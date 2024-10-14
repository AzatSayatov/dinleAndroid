package tm.bent.dinle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants.ACCESS_TOKEN_KEY
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreConstants.FIRST_TIME
import tm.bent.dinle.data.local.datastore.PreferenceDataStoreHelper
import tm.bent.dinle.data.remote.repository.SongRepository
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper
) : ViewModel() {


    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker

    val token = preferenceDataStoreHelper.getPreference(ACCESS_TOKEN_KEY, "")
    val firstTime = preferenceDataStoreHelper.getPreference(FIRST_TIME, true)

    fun getPreferenceDataStoreHelper() = preferenceDataStoreHelper

    fun likeSong(id:String){
        viewModelScope.launch {
            try {
                songRepository.likeSong(id)
            }catch (e:Exception){
                Log.e("TAG", "likeSong: "+e.message )
            }
        }
    }

    fun passOnboarding() {
        viewModelScope.launch {
            preferenceDataStoreHelper.putPreference(FIRST_TIME, false)
        }
    }

    fun listen(id: String, download: Boolean = false) {
        viewModelScope.launch {
            try {
                songRepository.listen(id, download)
            } catch (e: Exception) {
                Log.e("TAG", "listen: " + e.message)
            }
        }
    }

    fun insertSong(song:Song) {
        viewModelScope.launch {
            (Dispatchers.IO){
                songRepository.insertSong(song = song)
            }
        }
    }
}