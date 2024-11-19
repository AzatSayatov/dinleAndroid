package tm.bent.dinle.data.remote.repository


import android.os.Build
import android.util.Log
import retrofit2.Response
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreHelper
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.Auth
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.Device
import tm.bent.dinle.domain.model.DeviceResponse
import tm.bent.dinle.domain.model.Message
import tm.bent.dinle.domain.model.User
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper,
) {
    suspend fun login(phone: String): BaseResponse<Message> {
        Log.i("UserRepository", "login: $phone")
        val requestBody = Auth(phone)
        return apiService.login(requestBody)
    }

    suspend fun verifyOTPAndProceed(phone: String, otpCode: Int, device: Device): Response<BaseResponse<User>> {
        val requestBody = Auth(phone, otpCode, device)
        Log.e("TAG", "verifyOTPAndProceed: "+requestBody )
        return apiService.checkOtp(requestBody)
    }

    suspend fun getProfile(): BaseResponse<User> {
        return apiService.getProfile()
    }

    suspend fun getDevices(): DeviceResponse? {
        try {
            val response = apiService.getDevices()
            Log.d("DeviceResponse", "Response from API: ${response.data}")

            return response.data
        } catch (e: Exception) {
            Log.e("DeviceResponse", "Error fetching devices", e)
            return null
        }
    }


    suspend fun removeDevice( id: String) {
        return apiService.removeDevice(id)
    }

    suspend fun removeDeviceOtp(phone: String, otpCode: Int) {
        val requestBody = Auth(phone, otpCode)
        return apiService.removeDeviceOtp(requestBody)
    }

    suspend fun logout() {
        return apiService.logout(Build.ID)
    }

}