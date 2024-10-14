package tm.bent.dinle.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import tm.bent.dinle.data.remote.paging.NewsPagingSource
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.News
import tm.bent.dinle.domain.model.SearchResponse
import javax.inject.Inject


class NewsRepository @Inject constructor(
    private val apiService: ApiService,
) {
    fun getNewsList(baseRequest: BaseRequest) = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        NewsPagingSource(
            baseRequest = baseRequest,
            apiService = apiService,
        )
    }.flow

    suspend fun getNewsDetail(id:String): BaseResponse<News> {
        return apiService.getNewsDetail(id)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 5
    }
}