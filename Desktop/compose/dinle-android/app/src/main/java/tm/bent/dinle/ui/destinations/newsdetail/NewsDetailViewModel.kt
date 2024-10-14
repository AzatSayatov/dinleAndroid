package tm.bent.dinle.ui.destinations.newsdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tm.bent.dinle.data.remote.repository.NewsRepository
import tm.bent.dinle.domain.model.News
import tm.bent.dinle.ui.states.UIState
import javax.inject.Inject



@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState<News>())
    val uiState: StateFlow<UIState<News>> = _uiState

    fun getNewsDetail(id:String) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                val res = newsRepository.getNewsDetail(id)
                _uiState.update { it.updateToLoaded(res.data) }
            } catch (e: Exception) {
                Log.e("TAG", "search: " + e.message)
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    fun loadNewsDetails(newsList: List<News>) {
        newsList.forEach { news ->
            getNewsDetail(news.id) // Загружаем детальную информацию по всем новостям
        }
    }

}