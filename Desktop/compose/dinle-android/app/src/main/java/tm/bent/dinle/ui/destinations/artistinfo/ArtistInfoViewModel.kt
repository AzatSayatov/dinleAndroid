package tm.bent.dinle.ui.destinations.artistinfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.ArtistRepository
import tm.bent.dinle.domain.model.ArtistInfo
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class ArtistInfoViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState<ArtistInfo>())
    val uiState: StateFlow<UIState<ArtistInfo>> = _uiState

    fun getArtistInfo(id:String) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = artistRepository.getArtistInfo(id)
                Log.e("TAG", "search: "+res )
                _uiState.update { it.updateToLoaded(res.data) }
            } catch (e: Exception) {
                Log.e("TAG", "search: " + e.message)
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

}