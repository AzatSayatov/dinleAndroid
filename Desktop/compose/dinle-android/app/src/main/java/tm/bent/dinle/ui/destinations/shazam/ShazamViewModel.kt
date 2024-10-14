package tm.bent.dinle.ui.destinations.shazam

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
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.states.UIState
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ShazamViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {


    fun getPlayerController() = playerController

    private val _uiState = MutableStateFlow(UIState<Song>())
    val uiState: StateFlow<UIState<Song>> = _uiState

    fun sendAudio(file: File){
        Log.e("TAG", "loginUser: dfhcgjvhkbjlnk" )

        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = songRepository.sendAudio(file)
                Log.e("TAG", "sendAudio: "+res )
                if (res.data.isNotEmpty()){
                    _uiState.update { it.updateToLoaded(res.data[0]) }
                }else{
                    _uiState.update { it.updateToFailure("") }
                }
            }catch (e:Exception){
                Log.e("TAG", "sendAudio: "+e.message )
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }


    fun updateToDefault(){
        _uiState.update { it.updateToDefault() }
    }
    companion object {
        const val NUMBER = "number"
    }
}