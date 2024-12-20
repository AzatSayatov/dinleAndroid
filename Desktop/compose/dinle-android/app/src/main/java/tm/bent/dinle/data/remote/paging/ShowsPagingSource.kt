package tm.bent.dinle.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Media
import java.io.IOException


class ShowsPagingSource (
    private val baseRequest: BaseRequest,
    private val apiService: ApiService,
): PagingSource<Int, Media>() {

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> {
        try{
            var nextPageNumber = params.key ?: 1

            val response = apiService.getShows(baseRequest.apply { page = nextPageNumber })

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