package tm.bent.dinle.ui.destinations.otp

import android.annotation.SuppressLint
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.BuildConfig
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants.NOTIFICATIONS_KEY
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants.PHONE_KEY
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants.ACCESS_TOKEN_KEY
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants.USER_ID_KEY
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreHelper
import tm.bent.dinle.data.remote.repository.UserRepository
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.Device
import tm.bent.dinle.domain.model.User
import tm.bent.dinle.ui.states.BaseUIState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(BaseUIState())
    val uiState: StateFlow<BaseUIState> = _uiState


    fun loginUser(phone: String) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                userRepository.login(phone)
                _uiState.update { it.updateToDefault() }
            } catch (e: Exception) {
                Log.e("TAG", "loginUser: "+e.message )
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    fun verify(phone: String, code: Int) {

        _uiState.update { it.updateToLoading() }

        viewModelScope.launch {
            try {
                val res = userRepository.verifyOTPAndProceed(phone, code, getDeviceInfo())
                Log.e("TAG", "verify: "+res.body())
                res.body()?.let { proceedUserData(it.data) }
                _uiState.update { it.updateToLoaded() }
            } catch (e: Exception) {
                Log.e("TAG", "verify: "+e.message )
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }


    fun proceedUserData(user: User) {

        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(ACCESS_TOKEN_KEY, user.token)
            preferenceDataStoreHelper.putPreference(USER_ID_KEY, user.userId)
            preferenceDataStoreHelper.putPreference(PHONE_KEY, user.phone)
            preferenceDataStoreHelper.putPreference(NOTIFICATIONS_KEY, user.notifications)
        }

    }

    fun removeDevice(id: String) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                userRepository.removeDevice(id)
                _uiState.update { it.updateToDefault() }
            } catch (e: Exception) {
                Log.e("TAG", "removeDevice: "+e.message )
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    fun removeDeviceOtp(phone: String, code: Int) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                userRepository.removeDeviceOtp(phone, code)

                _uiState.update { it.updateToLoaded() }
            } catch (e: Exception) {
                Log.e("TAG", "removeDeviceOtp: "+e.message )
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceInfo(): Device {
        val deviceId = Build.ID
        val deviceName = Build.MODEL
        val osVersion = Build.VERSION.RELEASE


        val info = Device(
            id = deviceId,
            os = "Android",
            name = deviceName,
            version = osVersion,
            )
        return info
    }

    fun updateToDefault() {
        _uiState.update { it.updateToDefault() }
    }

}