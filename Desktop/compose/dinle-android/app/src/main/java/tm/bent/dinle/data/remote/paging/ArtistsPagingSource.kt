package tm.bent.dinle.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.model.Artist
import tm.bent.dinle.domain.model.BaseRequest
import java.io.IOException


class ArtistsPagingSource (
    private val baseRequest: BaseRequest,
    private val apiService: ApiService,
): PagingSource<Int, Artist>() {

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        try{

            Log.e("TAG", "load: "+baseRequest )
            var nextPageNumber = params.key ?: 1

            val response = if (baseRequest.isLiked != true) {
                apiService.getArtists(baseRequest.apply { page = nextPageNumber })
            } else {
                Log.e("TAG_request following artists", "load: $baseRequest")
                apiService.getFavoriteArtists(pageSize = 50, page = nextPageNumber)
            }

            return LoadResult.Page(
                data = response.data.rows,
                prevKey = null,
                nextKey = if (response.data.rows.isEmpty()) null else nextPageNumber + 1
            )

        }catch (e: IOException){
            Log.e("ArtistsPagingSource", e.message ?: "error")
            return LoadResult.Error(e)
        } catch (e: HttpException){
            Log.e("ArtistsPagingSource",  e.message ?: "error")
            return LoadResult.Error(e)
        }
    }

}