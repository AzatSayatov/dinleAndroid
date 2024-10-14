package tm.bent.dinle.ui.destinations.searchresults

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.SearchRepository
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.SearchResponse
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val _uiState = MutableStateFlow(UIState<SearchResponse>())
    val uiState: StateFlow<UIState<SearchResponse>> = _uiState

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest()).onEach {body->
        Log.e("TAG", ": "+body)
        if (!body.search.isNullOrEmpty()) search(body)
    }

    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker

    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }

    fun listen(id:String, download:Boolean = false){
        viewModelScope.launch {
            try {
                searchRepository.listen(id, download)
            }catch (e:Exception){
                Log.e("TAG", "listen: "+e.message )
            }
        }
    }

    fun search(body: BaseRequest) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = searchRepository.search(body)
                _uiState.update { it.updateToLoaded(res.data) }
            } catch (e: Exception) {
                Log.e("TAG", "search: " + e.message)
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    fun insertSong(song: Song) {
        viewModelScope.launch {
            (Dispatchers.IO){
                searchRepository.insertSong(song = song)

            }
        }
    }

}