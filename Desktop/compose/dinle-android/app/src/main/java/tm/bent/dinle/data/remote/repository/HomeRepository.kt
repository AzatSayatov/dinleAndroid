package tm.bent.dinle.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import tm.bent.dinle.data.remote.paging.HomePagingSource
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.dao.SongDao
import tm.bent.dinle.domain.model.Banner
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.domain.model.Home
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.User
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val apiService: ApiService,
    private val songDao: SongDao

) {
    fun getPagingHome() = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        HomePagingSource(
            baseRequest = BaseRequest(pageSize = 10),
            apiService = apiService,
        )
    }.flow

    suspend fun getHome(baseRequest: BaseRequest): BaseResponse<List<Home>> {
        return apiService.getHome(baseRequest)
    }
    suspend fun getBanners(): BaseResponse<List<Banner>> {
        return apiService.getBanners()
    }
    suspend fun getProfile(): BaseResponse<User> {
        return apiService.getProfile()
    }
    suspend fun listen(id:String, download :Boolean = false){
        apiService.listen(BaseRequest(songId = id, download = download))
    }

    suspend fun insertSong(song: Song) {
        return songDao.insertSong(song)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 3
    }
}
