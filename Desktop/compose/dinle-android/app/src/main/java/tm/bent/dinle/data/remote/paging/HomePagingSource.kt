package tm.bent.dinle.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tm.bent.dinle.data.remote.repository.HomeRepository.Companion.DEFAULT_PAGE_SIZE
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Home
import java.io.IOException


class HomePagingSource(
    private val baseRequest: BaseRequest,
    private val apiService: ApiService,
) : PagingSource<Int, Home>() {

    override fun getRefreshKey(state: PagingState<Int, Home>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Home> {
        try {
            val nextPageNumber = params.key ?: 1

            val response = apiService.getPagingHome(baseRequest.apply {
                page = nextPageNumber
                pageSize = DEFAULT_PAGE_SIZE
            })

            val data = response.data.rows

            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.isEmpty()) null else nextPageNumber + 1
            )

        } catch (e: IOException) {
            Log.e("HomePagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("HomePagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        }
    }

}