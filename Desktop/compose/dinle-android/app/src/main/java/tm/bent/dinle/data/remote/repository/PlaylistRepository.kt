package tm.bent.dinle.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import tm.bent.dinle.data.remote.paging.AlbumsPagingSource
import tm.bent.dinle.data.remote.paging.PlaylistsPagingSource
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.dao.SongDao
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.SongRequest
import tm.bent.dinle.domain.model.SongResponce
import javax.inject.Inject


class PlaylistRepository @Inject constructor(
    private val apiService: ApiService,
    private val songDao: SongDao

) {

    fun getPlaylists(baseRequest: BaseRequest) = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        PlaylistsPagingSource(
            baseRequest = baseRequest,
            apiService = apiService,
        )
    }.flow

    suspend fun getMiksPlaylist(request: SongRequest): SongResponce {
            return apiService.getMiksPLaylist(request)
    }


    fun getAlbums(baseRequest: BaseRequest) = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        AlbumsPagingSource(
            baseRequest = baseRequest,
            apiService = apiService,
        )
    }.flow
    suspend fun listen(id:String, download :Boolean = false){
        apiService.listen(BaseRequest(songId = id, download = download))
    }

    suspend fun insertSong(song: Song) {
        return songDao.insertSong(song)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 5
    }
}