package tm.bent.dinle.ui.destinations.newslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import tm.bent.dinle.data.remote.repository.NewsRepository
import tm.bent.dinle.data.remote.repository.ShowRepository
import tm.bent.dinle.domain.model.BaseRequest
import javax.inject.Inject


@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val baseRequest = savedStateHandle.getStateFlow("baseRequest", BaseRequest())

    @OptIn(ExperimentalCoroutinesApi::class)
    val newsList = baseRequest
        .flatMapLatest { baseRequest ->
            newsRepository.getNewsList(baseRequest)
        }
        .cachedIn(viewModelScope)


    fun setBaseRequest(baseRequest: BaseRequest) {
        savedStateHandle["baseRequest"] = baseRequest
    }
}
