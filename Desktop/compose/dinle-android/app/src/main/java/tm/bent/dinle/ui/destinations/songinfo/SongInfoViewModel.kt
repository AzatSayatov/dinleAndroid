package tm.bent.dinle.ui.destinations.songinfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.SongRepository
import tm.bent.dinle.domain.model.SongInfo
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject



@HiltViewModel
class SongInfoViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState<SongInfo>())
    val uiState: StateFlow<UIState<SongInfo>> = _uiState

    fun getSongInfo(id:String) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = songRepository.getSongInfo(id)
                _uiState.update { it.updateToLoaded(res.data) }
            } catch (e: Exception) {
                Log.e("TAG", "getSongInfo: " + e.message)
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

}