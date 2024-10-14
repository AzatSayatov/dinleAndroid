package tm.bent.dinle.ui.destinations.artists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.ArtistRepository
import tm.bent.dinle.data.remote.repository.HomeRepository
import tm.bent.dinle.domain.model.BaseRequest
import javax.inject.Inject


@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest())

    @OptIn(ExperimentalCoroutinesApi::class)
    val artists = baseRequest
        .flatMapLatest { baseRequest ->
            artistRepository.getArtists(baseRequest)
        }
        .cachedIn(viewModelScope)


    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }




}
