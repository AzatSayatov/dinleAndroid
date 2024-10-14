package tm.bent.dinle.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Media
import tm.bent.dinle.domain.model.News
import java.io.IOException


class NewsPagingSource (
    private val baseRequest: BaseRequest,
    private val apiService: ApiService,
): PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        try{
            var nextPageNumber = params.key ?: 1

            val response = apiService.getNewsList(baseRequest.apply { page = nextPageNumber })

            return LoadResult.Page(
                data = response.data.rows,
                prevKey = null,
                nextKey = if (response.data.rows.isEmpty()) null else nextPageNumber+1
            )

        }catch (e: IOException){
            Log.e("PlaylistsPagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        } catch (e: HttpException){
            Log.e("PlaylistsPagingSource",  e.message ?: "error")
            return LoadResult.Error(e)
        }
    }

}