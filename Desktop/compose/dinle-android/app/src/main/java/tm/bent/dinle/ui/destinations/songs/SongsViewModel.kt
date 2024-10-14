package tm.bent.dinle.ui.destinations.songs

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tm.bent.dinle.data.remote.repository.PlaylistRepository
import tm.bent.dinle.data.remote.repository.SongRepository
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.FetchSong
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.domain.model.ListenSong
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.SongIn
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class SongsViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    ) : ViewModel() {

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest())

    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker

    private val _lastSeenSongs = MutableStateFlow<List<Song>?>(null)
    val lastSeenSongs: StateFlow<List<Song>?> = _lastSeenSongs


//    private val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)



    @OptIn(ExperimentalCoroutinesApi::class)
    val songs = baseRequest
        .flatMapLatest { body ->
            songRepository.getSongs(body)
        }
        .cachedIn(viewModelScope)

    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }

    fun likeSong(id:String){
        viewModelScope.launch {
            try {
                songRepository.likeSong(id)
            }catch (e:Exception){
                Log.e("TAG", "likeSong: "+e.message )
            }
        }
    }

    fun updateSongList(newSongsList: List<Song>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                songRepository.updateSongsOrder(newSongsList)
            } catch (e: Exception) {
                Log.e("SongsViewModel", "updateSongList: ${e.message}")
            }
        }
    }

    fun listen(id:String, download:Boolean = false){
        viewModelScope.launch {
            try {
                Log.d("SongsViewModel", "Listening to song with ID: $id")
                songRepository.listen(id, download)
            }catch (e:Exception){
                Log.e("TAG", "listen: "+e.message )
            }
        }
    }

    fun getLastSeenSongs() {
        viewModelScope.launch {
            try {
                val songs = songRepository.getLastSeenSongs() // Получение последних прослушанных песен
                _lastSeenSongs.value = songs
            } catch (e: Exception) {
                Log.e("SongsViewModel", "getLastSeenSongs: ${e.message}")
                _lastSeenSongs.value = null
            }
        }
    }

    fun listenedSong(id: String){
        viewModelScope.launch {
            try {
                Log.d("SongsViewModel", "listenedSong: $id")
                songRepository.listenedSong(id)
            }catch (e: Exception){
                Log.e("TAG", "listenedSong: "+ e.message )
            }
        }
    }

    fun insertSong(song: Song) {
        viewModelScope.launch {
            (Dispatchers.IO){
                songRepository.insertSong(song = song)

            }
        }
    }
}
