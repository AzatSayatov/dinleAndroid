package tm.bent.dinle.ui.destinations.playlists

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.ArtistRepository
import tm.bent.dinle.data.remote.repository.PlaylistRepository
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.News
import tm.bent.dinle.domain.model.SongRequest
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest()).onEach {
    }

    private val _uiState = MutableStateFlow(UIState<News>())
    val uiState: StateFlow<UIState<News>> = _uiState
//    val apiService

    @OptIn(ExperimentalCoroutinesApi::class)
    val playlists = baseRequest
        .flatMapLatest { baseRequest ->
            playlistRepository.getPlaylists(baseRequest)
        }
        .cachedIn(viewModelScope)


    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }

    fun getMiksPlaylist() {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = playlistRepository.getMiksPlaylist(SongRequest(2, 200, "", "miks", "", "", ""))
//                _uiState.update { it.updateToLoaded(res.data) }
            } catch (e: Exception) {
                Log.e("TAG", "search: " + e.message)
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

//    viewModelScope.launch {
//        try {
//            val response = apiService.getSongs(SongRequest(2, 200, "", "miks", "", "", ""))
//            // Работа с данными
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

}
