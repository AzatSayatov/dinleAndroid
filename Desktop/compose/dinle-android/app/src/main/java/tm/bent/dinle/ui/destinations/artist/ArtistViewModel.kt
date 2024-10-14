package tm.bent.dinle.ui.destinations.artist

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.ArtistRepository
import tm.bent.dinle.domain.model.ArtistDetail
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState<ArtistDetail>())
    val uiState: StateFlow<UIState<ArtistDetail>> = _uiState

    fun getPlayerController() = playerController

    fun getDownloadTracker() = downloadTracker

    fun getArtistDetail(body: BaseRequest) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = artistRepository.getArtistDetail(body)
                Log.e("TAG", "search: "+res )
                _uiState.update { it.updateToLoaded(res.data) }
            } catch (e: Exception) {
                Log.e("TAG", "search: " + e.message)
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }




    fun listen(id:String, download:Boolean = false){
        viewModelScope.launch {
            try {
                artistRepository.listen(id, download)
            }catch (e:Exception){
                Log.e("TAG", "listen: "+e.message )
            }
        }
    }

    fun subscribe(id:String){
        viewModelScope.launch {
            try {
                artistRepository.subscribe(BaseRequest(artistId = id))
            }catch (e:Exception){
                Log.e("TAG", "subscribe: "+e.message )
            }
        }
    }
    fun deleteSong(song: Song){
        viewModelScope.launch {
            try {
                artistRepository.deleteSong(song)
            }catch (e: Exception){

            }
        }
    }

    fun insertSong(song: Song) {
        viewModelScope.launch {
            (Dispatchers.IO){
                artistRepository.insertSong(song = song)

            }
        }
    }

}