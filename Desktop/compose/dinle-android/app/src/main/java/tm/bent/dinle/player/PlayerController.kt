package tm.bent.dinle.player

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.compose.LazyPagingItems

import tm.bent.dinle.ui.util.collectPlayerState
import tm.bent.dinle.ui.util.launchPlaybackStateJob
import tm.bent.dinle.ui.util.resetTracks
import tm.bent.dinle.ui.util.swap
import tm.bent.dinle.ui.util.toMediaItemList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tm.bent.dinle.di.AudioPlayer
import tm.bent.dinle.domain.model.Song
import javax.inject.Inject
import kotlin.random.Random


@OptIn(UnstableApi::class)
@HiltViewModel
class PlayerController @Inject constructor(
    private val myPlayer: MyPlayer,
) : ViewModel(), PlayerEvents {

    private val _tracks = mutableStateListOf<Song>()
    private val _tracksShuffled = mutableStateListOf<Song>()
    private val tracksForShuffle = mutableStateListOf<Song>()
    val originalTracks = mutableStateListOf<Song>()



    val tracks: List<Song> get() = _tracks
    val tracksShuffle: List<Song> get() = _tracksShuffled

    private var isTrackPlay: Boolean = false
    var selectedTrack: Song? by mutableStateOf(null)
        private set

    var selectedTrackIndex: Int by mutableStateOf(-1)

    private var playbackStateJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState(0L, 0L))

    val playbackState: StateFlow<PlaybackState> get() = _playbackState

    private var isAuto: Boolean = false

    private var _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled

    private var _repeatMode = MutableStateFlow(REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode


    fun init(track: Song, songs: List<Song>) {
        myPlayer.iniPlayer(songs.toMediaItemList())
        observePlayerState()

        if (!_tracks.isEmpty()) {
            _tracks.removeRange(0, tracks.size)
            tracksForShuffle.removeRange(0, tracks.size)
        }
        _tracks.addAll(songs)
        tracksForShuffle.addAll(songs)
        selectedTrackIndex = _tracks.indexOf(track)
        selectedTrackIndex = tracksForShuffle.indexOf(track)
        selectedTrack = _tracks[selectedTrackIndex]

        setUpTrack()
    }

    fun init(track: Int, songs: List<Song>) {
        if (!_tracks.isEmpty()) {
            _tracks.removeRange(0, tracks.size)
            _tracksShuffled.removeRange(0, tracks.size)
        }
        _tracks.addAll(songs)
        _tracksShuffled.addAll(songs)
        myPlayer.iniPlayer(songs.toMediaItemList())
        observePlayerState()
        onTrackClick(song = track)
        setUpTrack()
    }

    fun initShuffle(track: Int, songs: List<Song>){
        if (!_tracksShuffled.isEmpty()) {
            _tracksShuffled.removeRange(0, tracks.size)
        }
        _tracksShuffled.addAll(songs)
        _tracksShuffled.shuffle()
        myPlayer.iniPlayer(_tracksShuffled.toMediaItemList())
        observePlayerState()
        onTrackClick(song = track)
        setUpTrack()
    }

    fun addSongAfterCurrent(song: Song) {
        selectedTrackIndex?.let { index ->
            if (index < _tracks.size - 1) {
                val newIndex = index + 1
                _tracks.add(newIndex, song)
                tracksForShuffle.add(newIndex, song)
                myPlayer.addMediaItem(newIndex, song.toMediaItem())
            } else {
                // Add to the end of the list if the current index is the last one
                _tracks.add(song)
                tracksForShuffle.add(song)
                myPlayer.addMediaItem(0,song.toMediaItem())
            }
        }
    }

    fun playSong(song: Song) {
        // Логика для проигрывания конкретной песни
        getPlayer().setMediaItem(song.toMediaItem())
        getPlayer().prepare()
        getPlayer().play()
    }

    fun isPlaying(): Boolean{
        return getPlayer().isPlaying
    }
    fun pauseSong() {
        return getPlayer().pause()
    }


    fun onReorder(from: Int, to: Int) {
        selectedTrackIndex = _tracks.indexOf(selectedTrack)
        selectedTrackIndex = tracksForShuffle.indexOf(selectedTrack)
    }

    fun onMove(from: Int, to: Int) {
        // Сохраняем индекс текущей проигрываемой песни
        val currentPlayingIndex = selectedTrackIndex

        // Перемещаем песню
        val song = tracks[from]
        _tracks.swap(from, to)
        tracksForShuffle.swap(from, to)
        myPlayer.reOrder(from, to, song.toMediaItem())

        // Восстанавливаем индекс проигрываемой песни
        selectedTrackIndex = currentPlayingIndex
        selectedTrack = tracks[selectedTrackIndex] // обновляем ссылку на проигрываемую песню
    }

    fun setRepeatMode(mode: Int) {
        _repeatMode.value = mode
        return myPlayer.setRepeatMode(mode)

    }

    fun getRepeatMode(): Int {
        return _repeatMode.value
    }

    fun playerCurrentTime(): Long {
        return myPlayer.currentPlaybackPosition
    }

    fun playerTrackDuration(): Long {
        return myPlayer.currentTrackDuration
    }

    fun getPlayer(): ExoPlayer {
        return myPlayer.getPlayer()
    }

    /**
     * Handles track selection.
     *
     * @param index The index of the selected track in the track list.
     */
    private fun onTrackSelected(index: Int) {
        Log.e("TAG", "onTrackSelected: "+index)
        if (selectedTrackIndex == -1) isTrackPlay = true
        if (selectedTrackIndex == -1 || selectedTrackIndex != index) {
            _tracks.resetTracks()
            tracksForShuffle.resetTracks()
            selectedTrackIndex = index
            selectedTrack = tracks[selectedTrackIndex]
            setUpTrack()
        }
    }

    private fun setUpTrack() {
        if (selectedTrackIndex in _tracks.indices) {
            if (!isAuto) {
                myPlayer.setUpTrack(selectedTrackIndex, isTrackPlay)
            }
        } else {
            Log.e("PlayerController", "Invalid selectedTrackIndex: $selectedTrackIndex")
        }
    }

    /**
     * Updates the playback state and launches or cancels the playback state job accordingly.
     *
     * @param state The new player state.
     */
    private fun updateState(state: PlayerStates) {
        Log.e("TAG", "updateState: ")
        if (selectedTrackIndex in _tracks.indices) {
            isTrackPlay = state == PlayerStates.STATE_PLAYING
            _tracks[selectedTrackIndex].state = state
            tracksForShuffle[selectedTrackIndex].state = state
            _tracks[selectedTrackIndex].isSelected = true
            tracksForShuffle[selectedTrackIndex].isSelected = true
            selectedTrack = null
            selectedTrack = tracks[selectedTrackIndex]

            if (state == PlayerStates.STATE_NEXT_TRACK) {
                Log.e("TAG", "updateState: StartNExt $selectedTrackIndex")


                if (_shuffleEnabled.value){

                }else{
                    if (selectedTrackIndex < tracks.size - 1) {
                        Log.e("TAG", "onNextClick: ")
                        onTrackSelected(selectedTrackIndex + 1)

                        observePlayerState()
                    }
                }

                Log.e("ShuffleModeStateNext", "setShuffleMode: enabled", )


                myPlayer.emitPlaying()
                setUpTrack()
            }
            updatePlaybackState(state)

            if (state == PlayerStates.STATE_END && myPlayer.getRepeatMode() == REPEAT_MODE_ALL) {
                Log.e("TAG", "updateState: StateEnd $selectedTrackIndex")

                onTrackSelected(0)
            }
        } else {
            Log.e("TAG", "updateState: Invalid selectedTrackIndex $selectedTrackIndex")
        }
    }



    fun setShuffleMode() {
        Log.e("ShuffleMode", "setShuffleMode: enabled", )

            if (_shuffleEnabled.value) {
                myPlayer.iniPlayer(_tracks.shuffled().toMediaItemList())
                setUpTrack()
            } else {
                myPlayer.iniPlayer(_tracks.toMediaItemList())
                setUpTrack()
            }

    }

    fun toggleShuffle(song: Song) {
        _shuffleEnabled.value = !_shuffleEnabled.value
        _tracksShuffled.shuffle()
        selectedTrack = song
        selectedTrackIndex = _tracks.indexOf(selectedTrack)
    }

    private fun observePlayerState() {
        viewModelScope.collectPlayerState(myPlayer, ::updateState)
    }

    private fun updatePlaybackState(state: PlayerStates) {
        playbackStateJob?.cancel()
        playbackStateJob = viewModelScope.launchPlaybackStateJob(_playbackState, state, myPlayer)
    }


    /**
     * Implementation of [PlayerEvents.onPreviousClick].
     * Changes to the previous track if one exists.
     */
    override fun onPreviousClick() {
        if (selectedTrackIndex > 0) onTrackSelected(selectedTrackIndex - 1)
    }

    /**
     * Implementation of [PlayerEvents.onNextClick].
     * Changes to the next track in the list if one exists.
     */
    override fun onNextClick() {
        if (_shuffleEnabled.value){
            if (selectedTrackIndex == -1) isTrackPlay = true
            if (selectedTrackIndex == -1 ) {
                _tracksShuffled.resetTracks()
                selectedTrack = tracksShuffle[selectedTrackIndex]
                setUpTrack()
            }
        }else{
            if (selectedTrackIndex < tracks.size - 1) {
                Log.e("TAG", "onNextClick: ")
                onTrackSelected(selectedTrackIndex + 1)

                observePlayerState()
            } else {

                if (tracks.isNotEmpty() && myPlayer.getRepeatMode() == REPEAT_MODE_ALL) {
                    onTrackSelected(0)
                }
            }
        }

    }


    override fun onPlayPauseClick() {
        myPlayer.playPause()
    }

    override fun onTrackClick(song: Song) {
        Log.e("TAG", "onTrackClick: "+tracks.indexOf(song))
        onTrackSelected(tracks.indexOf(song))
    }

    override fun onTrackClick(song: Int) {

        onTrackSelected(song)
    }

    override fun onPlayNext(song: Song) {

        if (_tracks.contains(song)) {
            if (selectedTrackIndex == _tracks.indexOf(song)) return
            onMove(_tracks.indexOf(song), selectedTrackIndex + 1)
        } else {
            _tracks.add(selectedTrackIndex + 1, song)
            myPlayer.addMediaItem(selectedTrackIndex + 1, song.toMediaItem())
        }

    }


    override fun onSeekBarPositionChanged(position: Long) {
        viewModelScope.launch {
            myPlayer.seekToPosition(position)
        }
    }


}