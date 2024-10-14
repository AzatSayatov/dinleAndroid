package tm.bent.dinle.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Playlist
import java.io.IOException


class PlaylistsPagingSource (
    private val baseRequest: BaseRequest,
    private val apiService: ApiService,
): PagingSource<Int, Playlist>() {

    override fun getRefreshKey(state: PagingState<Int, Playlist>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Playlist> {
        try{
            var nextPageNumber = params.key ?: 1

            val response = if(baseRequest.isLiked != true){
                apiService.getPlaylists(baseRequest.apply { page = nextPageNumber })
            } else{
                apiService.getFavoritePlaylist(nextPageNumber)
            }

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