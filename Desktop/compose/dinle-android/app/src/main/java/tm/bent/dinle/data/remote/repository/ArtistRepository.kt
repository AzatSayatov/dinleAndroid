package tm.bent.dinle.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import tm.bent.dinle.data.remote.paging.ArtistsPagingSource
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.dao.SongDao
import tm.bent.dinle.domain.model.ArtistDetail
import tm.bent.dinle.domain.model.ArtistInfo
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.Song
import javax.inject.Inject


class ArtistRepository @Inject constructor(
    private val apiService: ApiService,
    private val songDao: SongDao
) {
    fun getArtists(baseRequest: BaseRequest) = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        ArtistsPagingSource(
            baseRequest = baseRequest,
            apiService = apiService,
        )
    }.flow

    suspend fun getArtistDetail(body: BaseRequest): BaseResponse<ArtistDetail> {
        return apiService.getArtistDetail(body)
    }

    suspend fun getArtistInfo(id:String): BaseResponse<ArtistInfo> {
        return apiService.getArtistInfo(id)
    }
    suspend fun subscribe(baseRequest: BaseRequest) {
        return apiService.subscribe(baseRequest)
    }
    suspend fun listen(id:String, download :Boolean = false){
        apiService.listen(BaseRequest(songId = id, download = download))
    }
//    suspend fun getFavouriteArtists(id: Int): PagingResponse<Artist>{
//        return apiService.getFavoriteArtists(id)
//    }

    suspend fun insertSong(song: Song) {
        return songDao.insertSong(song)
    }
    suspend fun deleteSong(song: Song){
        return songDao.deleteSong(song)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 5
    }
}