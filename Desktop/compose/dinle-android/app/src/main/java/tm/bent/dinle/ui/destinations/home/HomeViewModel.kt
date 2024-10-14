package tm.bent.dinle.ui.destinations.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.HomeRepository
import tm.bent.dinle.data.remote.repository.SongRepository
import tm.bent.dinle.domain.model.Banner
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Home
import tm.bent.dinle.domain.model.PagingData
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val songRepository: SongRepository,
    private val playerController: PlayerController,
    private val downloadTracker: DownloadTracker,
    private val savedStateHandle: SavedStateHandle
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState<List<Home>>())
    val uiState: StateFlow<UIState<List<Home>>> = _uiState

    val banners = MutableStateFlow<List<Banner>>(emptyList())

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest(
        pageSize = 100,
        playlistId = "miks"
    ))

    @OptIn(ExperimentalCoroutinesApi::class)
    val mixSongs = baseRequest
        .flatMapLatest { body ->
            songRepository.getSongs(body)
        }
        .cachedIn(viewModelScope)


    init {
        getProfile()
        getHome()
//        getBanners()
    }


    fun getPlayerController() = playerController
    fun getDownloadTracker() = downloadTracker

    fun getHome() {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = homeRepository.getHome(BaseRequest(pageSize = 10))
                _uiState.update { it.updateToLoaded(res.data) }
            } catch (e: Exception) {
                Log.e("TAG", "getHome: "+e )
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    private fun getBanners() {
        viewModelScope.launch {
            try {
                val res = homeRepository.getBanners()
               banners.update { res.data }
            } catch (e: Exception) {
                Log.e("TAG", "getBanners: "+e )
            }
        }
    }

    fun listen(id:String, download:Boolean = false){
        viewModelScope.launch {
            try {
                songRepository.listen(id, download)
            }catch (e:Exception){
                Log.e("TAG", "listen: "+e.message )
            }
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            try {
                homeRepository.getProfile()
            } catch (e: Exception) {
                Log.e("TAG", "getProfile: "+e )
            }
        }
    }

    fun insertSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                songRepository.insertSong(song)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error inserting song: ${e.message}")
            }
        }
    }
}
