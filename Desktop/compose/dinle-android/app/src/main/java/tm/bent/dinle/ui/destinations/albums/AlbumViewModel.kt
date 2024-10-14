package tm.bent.dinle.ui.destinations.albums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import tm.bent.dinle.data.remote.repository.HomeRepository
import tm.bent.dinle.data.remote.repository.PlaylistRepository
import tm.bent.dinle.domain.model.BaseRequest
import javax.inject.Inject



@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest())

    @OptIn(ExperimentalCoroutinesApi::class)
    val albums = baseRequest
        .flatMapLatest { baseRequest ->
            playlistRepository.getAlbums(baseRequest)
        }
        .cachedIn(viewModelScope)



    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }
}

