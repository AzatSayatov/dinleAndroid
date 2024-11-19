package tm.bent.dinle.ui.destinations.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.UserRepository
import tm.bent.dinle.ui.states.BaseUIState
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var phone = savedStateHandle.getLiveData<String>(NUMBER).asFlow()

    private val _uiState = MutableStateFlow(BaseUIState())
    val uiState: StateFlow<BaseUIState> = _uiState

    fun setPhoneNumber(number: String){
        savedStateHandle[NUMBER] = number
    }

    fun loginUser(phone: String){
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                userRepository.login("+993$phone")
                _uiState.update { it.updateToLoaded() }
                Log.e("TAG", "loginUser: $phone" )

            }catch (e:Exception){

                Log.e("TAT", "loginUser: "+e.message )
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