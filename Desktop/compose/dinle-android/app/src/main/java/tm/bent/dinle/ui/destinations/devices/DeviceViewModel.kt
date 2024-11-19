package tm.bent.dinle.ui.destinations.devices

import android.annotation.SuppressLint
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tm.bent.dinle.hinlen.BuildConfig
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreHelper
import tm.bent.dinle.data.remote.repository.UserRepository
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.Device
import tm.bent.dinle.domain.model.DeviceResponse
import tm.bent.dinle.ui.states.UIState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper
): ViewModel() {

    private val _uiState = MutableStateFlow(UIState<DeviceResponse>())
    val uiState: StateFlow<UIState<DeviceResponse>> = _uiState

    val phone = preferenceDataStoreHelper.getPreference(PreferenceDataStoreConstants.PHONE_KEY, "")

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dinle.com.tm/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val _currentDevice = mutableStateOf<Device?>(null)
    val currentDevice: State<Device?> get() = _currentDevice

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _gdevices = MutableLiveData<DeviceResponse>()
    val gdevices: LiveData<DeviceResponse> get() = _gdevices

    init {
        fetchDevices()
    }

    fun fetchDevices() {
        viewModelScope.launch {
            Log.d("DeviceViewModel", "Fetching devices started")
            try {
                val deviceResponse = userRepository.getDevices()
                Log.i("DeviceViewModel", "fetchDevices: $deviceResponse")
                deviceResponse?.let {
                    _gdevices.postValue(it)
                    _devices.value = it.devices // Обновляем список устройств
                }
            } catch (e: Exception) {
                Log.e("DeviceViewModel", "Error in fetchDevices", e)
            }
            Log.d("DeviceViewModel", "Fetching devices ended")
        }
    }



    @SuppressLint("HardwareIds")
    fun getDeviceInfo(): Device {
        val deviceId = Build.ID
        val deviceName = Build.MODEL
        val osVersion = Build.VERSION.RELEASE
        val version: String = BuildConfig.VERSION_NAME
        val uptimeMillis = SystemClock.elapsedRealtime()
        val lastActivityTimeInMillis = System.currentTimeMillis() - uptimeMillis

        // Преобразуем в читаемую дату
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val lastActivityDate = dateFormat.format(Date(lastActivityTimeInMillis)).toString()

        val info = Device(
            id = deviceId,
            os = "Android",
            name = deviceName,
            version = osVersion,
            lastActivity = lastActivityDate
        )
        return info
    }

    companion object {
        const val NUMBER = "number"
    }
}