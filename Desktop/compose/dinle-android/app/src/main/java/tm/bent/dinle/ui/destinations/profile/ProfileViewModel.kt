package tm.bent.dinle.ui.destinations.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import tm.bent.dinle.BuildConfig
import tm.bent.dinle.MainActivity
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants.LANGUAGE_KEY
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreHelper
import tm.bent.dinle.data.remote.repository.SettingsRepository
import tm.bent.dinle.data.remote.repository.UserRepository
import tm.bent.dinle.domain.model.Device
import tm.bent.dinle.domain.model.User
import tm.bent.dinle.ui.util.LocaleHelper
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val context: Context,
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,

) : ViewModel() {

    val phone = preferenceDataStoreHelper.getPreference(PreferenceDataStoreConstants.PHONE_KEY, "")
    val notifications = preferenceDataStoreHelper.getPreference(PreferenceDataStoreConstants.NOTIFICATIONS_KEY,true)
    var lang = settingsRepository.locale



    fun proceedUserData(user: User) {

        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(
                PreferenceDataStoreConstants.ACCESS_TOKEN_KEY,
                user.token
            )
            preferenceDataStoreHelper.putPreference(
                PreferenceDataStoreConstants.USER_ID_KEY,
                user.userId
            )
            preferenceDataStoreHelper.putPreference(
                PreferenceDataStoreConstants.PHONE_KEY,
                user.phone
            )
            preferenceDataStoreHelper.putPreference(
                PreferenceDataStoreConstants.NOTIFICATIONS_KEY,
                user.notifications
            )
        }

    }


    fun toggleNotifications(notifications: Boolean) {
        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(PreferenceDataStoreConstants.NOTIFICATIONS_KEY, notifications )
        }

    }

    @SuppressLint("HardwareIds")
    fun getDeviceInfo(): Device {
        val deviceId = Build.ID
        val deviceName = Build.DEVICE
        val version: String = BuildConfig.VERSION_NAME

        val info = Device(
            id = deviceId,
            os = "Android",
            name = deviceName,
            version = version,
        )
        return info
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            try {
                userRepository.logout()
                restartApplication(context)
                Log.i("LogOut", "logout: boldy")
            } catch (e: Exception) {
                Log.e("TAG", "logout: ${e.message}")
            }
        }
    }

    fun restartApplication(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        Runtime.getRuntime().exit(0)  // Принудительно завершить приложение
    }
    fun clearUserData() {
        viewModelScope.launch {
            try {
                preferenceDataStoreHelper.clearAllPreference()
                Log.i("ClearData", "User data cleared successfully.")
            } catch (e: Exception) {
                Log.e("ClearData", "Error clearing user data: ${e.message}", e)
            }
        }
    }


    fun setLocale(lang: String) {
        LocaleHelper.setLocale(context, lang)
        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(LANGUAGE_KEY, lang)
        }
    }
}

