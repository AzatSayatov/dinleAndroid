package tm.bent.dinle.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import tm.bent.dinle.data.remote.paging.MediaPagingSource
import tm.bent.dinle.data.remote.paging.ShowsPagingSource
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import javax.inject.Inject


class ShowRepository @Inject constructor(
    private val apiService: ApiService,
) {
    fun getShows(baseRequest: BaseRequest) = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        ShowsPagingSource(
            baseRequest = baseRequest,
            apiService = apiService,
        )
    }.flow

    fun getMedia(baseRequest: BaseRequest) = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        MediaPagingSource(
            baseRequest = baseRequest,
            apiService = apiService,
        )
    }.flow

    companion object {
        const val DEFAULT_PAGE_SIZE = 5
    }
}