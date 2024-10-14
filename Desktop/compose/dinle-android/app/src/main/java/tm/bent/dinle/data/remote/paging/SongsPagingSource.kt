package tm.bent.dinle.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import java.io.IOException

class SongsPagingSource (
    private val baseRequest: BaseRequest,
    private val apiService: ApiService,
): PagingSource<Int, Song>() {

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        try{
            var nextPageNumber = params.key ?: 1

            val response = if(baseRequest.isLiked != true){
                apiService.getSongs(baseRequest.apply { page = nextPageNumber })
            } else{
                apiService.getFavoriteSongs(nextPageNumber)
            }

            return LoadResult.Page(
                data = response.data.rows,
                prevKey = null,
                nextKey = if (response.data.rows.isEmpty()) null else nextPageNumber+1
            )
        }catch (e: IOException){
            Log.e("SongsPagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        } catch (e: HttpException){
            Log.e("SongsPagingSource",  e.message ?: "error")
            return LoadResult.Error(e)
        }
    }

}