package tm.bent.dinle.ui.destinations.videoplayer

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreHelper
import tm.bent.dinle.data.remote.repository.SongRepository
import tm.bent.dinle.di.AudioPlayer
import tm.bent.dinle.di.VideoPlayer
import tm.bent.dinle.player.MyPlayer
import tm.bent.dinle.player.PlayerController
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @VideoPlayer private val player: ExoPlayer,
    @AudioPlayer private val audioPlayer: ExoPlayer,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper
) : ViewModel() {

    private var playerState: PlayerState? = null


    data class PlayerState(val playBackposition: Long, val playWhenReady: Boolean)
    fun getPlayer() = player


    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    player.play()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                // Обработка ошибок
                error.printStackTrace() // или использовать логирование
            }
        })
    }

    private var currentVideoUrl: String? = null

    // ... (остальной код)

    fun playVideo(url: String) {
        Log.i("VideoPlayer", "playVideo: attempting to play $url")

        if (audioPlayer.isPlaying) audioPlayer.pause()

        // Проверяем, что URL отличается от текущего
        if (currentVideoUrl != url) {
            Log.i("VideoPlayer", "playVideo: new video detected, stopping current video")

            // Убедитесь, что плеер не освобожден
            if (player.isPlaying) {
                player.stop()
            }

            try {
                // Устанавливаем новый медиа-элемент
                val uri = Uri.parse(url)
                val mediaItem = MediaItem.fromUri(uri)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = true
                player.seekTo(0)

                currentVideoUrl = url
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error while setting media item: ${e.message}")
            }
        } else {
            Log.i("VideoPlayer", "playVideo: same video, not changing")
        }
    }




    fun saveState(){
        playerState = PlayerState(
            playBackposition = player.currentPosition,
            playWhenReady = player.playWhenReady
        )
    }

    fun restoreState(){
        playerState?.let { state->
            player.playWhenReady = state.playWhenReady
            player.seekTo(state.playBackposition)
        }
    }
    fun releasePlayer(){
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
//        player.release()
        player.stop()
    }

}

