package tm.bent.dinle.ui.destinations.genre

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.SearchRepository
import tm.bent.dinle.di.DataStoreUtil
import tm.bent.dinle.di.SearchDataStore
import tm.bent.dinle.domain.model.Banner
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject


@HiltViewModel
class GenreViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dataStoreUtil: DataStoreUtil
): ViewModel() {


    private val _uiState = MutableStateFlow(UIState<List<Genre>>())
    val uiState: StateFlow<UIState<List<Genre>>> = _uiState

    private val _uiStateBanner = MutableStateFlow(UIState<List<Banner>>())
    val uiStateBanner: StateFlow<UIState<List<Banner>>> = _uiStateBanner

    private val searchDataStore: SearchDataStore = SearchDataStore.getInstance(dataStoreUtil.dataStore)
    val keys = searchDataStore.searchFlow

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest())

    val banners = mutableStateOf<List<Banner>>(emptyList())


    init {
        getGenres(baseRequest.value)
        loadBanners()
    }



    fun loadBanners() {
        viewModelScope.launch {
            try {
                banners.value = searchRepository.getBanners()
            } catch (e: Exception) {
                e.printStackTrace() // Обработка ошибки
            }
        }
    }



    fun getGenres(baseRequest: BaseRequest = BaseRequest()){
        Log.e("TAG", "getGenrafdasfes: "+baseRequest )
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = searchRepository.getGenres(baseRequest)
                Log.e("TAG", "getGenres: "+res )
                _uiState.update { it.updateToLoaded(res.data) }
            }catch (e:Exception){
                Log.e("TAG", "getGenres: "+e.message )
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }



    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }

    fun saveSearchStr(s:String){
        viewModelScope.launch {
            searchDataStore.saveNewMsg(s)
        }
    }

//    fun clearSearchHistory() {
//        // Логика для очистки истории поиска
//        keys.value = Search.getDefaultInstance() // Пример очистки
//    }

    fun clearSearchStr(){
        viewModelScope.launch {
            searchDataStore.clearAllMyMessages()
        }
    }

    companion object {
        const val NUMBER = "number"
    }
}