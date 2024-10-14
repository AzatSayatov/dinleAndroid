package tm.bent.dinle.ui.destinations.playlistdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.SongRepository
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.domain.model.Playlist
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.mockPlaylist
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState<List<Genre>>())
    val uiState: StateFlow<UIState<List<Genre>>> = _uiState


    private val _playlistSongs = MutableStateFlow<List<Song>>(emptyList())
    val playlistSongs: StateFlow<List<Song>> = _playlistSongs

//    fun fetchSongsForPlaylist(playlistId: String) {
//        viewModelScope.launch {
//            try {
//                val songsList = songRepository.getSongsForPlaylist(playlistId)
//                _playlistSongs.value = songsList
//            } catch (e: Exception) {
//                Log.e("TAG", "Error fetching songs for playlist: ${e.message}")
//            }
//        }
//    }


    companion object {
        const val TYPE_ALBUM = "TYPE_ALBUM"
        const val TYPE_PLAYLIST = "TYPE_PLAYLIST"
    }
    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest())
    val playlist = savedStateHandle.getStateFlow("playlist", mockPlaylist)


    @OptIn(ExperimentalCoroutinesApi::class)
    val songs = baseRequest
        .flatMapLatest { body ->
            songRepository.getSongs(body)
        }
        .cachedIn(viewModelScope)

    fun getPlaylist(id:String, type:String){
        viewModelScope.launch {
            try {
                val res = if (type == TYPE_ALBUM){
                    songRepository.getAlbum(id)
                }else{
                    songRepository.getPlaylist(id)
                }
                Log.e("TAG", "getPlaylist: "+res)
                setPlaylist(res.data)

            }catch (e:Exception){
                Log.e("TAG", "likeSong: "+e.message )
            }
        }
    }
    fun setPlaylist(playlist: Playlist) {
        savedStateHandle["playlist"] = playlist
    }

    fun likePlaylist(id:String, type:String){
        viewModelScope.launch {
            try {
                if (type == TYPE_ALBUM){
                    songRepository.likeAlbum(id)
                }else{
                    songRepository.likePlaylist(id)
                }
            }catch (e:Exception){
                Log.e("TAG", "likeSong: "+e.message )
            }
        }
    }

    fun listen(id:String, download:Boolean = false){
        viewModelScope.launch {
            try {
                songRepository.listen(id, download)
            }catch (e:Exception){
                Log.e("TAG", "listen: "+e.message )
            }
        }
    }

    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }

    fun insertSong(song: Song) {
        viewModelScope.launch {
            (Dispatchers.IO){
                songRepository.insertSong(song = song)

            }
        }
    }

}
