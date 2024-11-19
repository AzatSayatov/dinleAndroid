package tm.bent.dinle.data.remote.repository

import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.dao.SongDao
import tm.bent.dinle.domain.model.Banner
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.domain.model.SearchResponse
import tm.bent.dinle.domain.model.Song
import javax.inject.Inject


class SearchRepository @Inject constructor(
    private val apiService: ApiService,
    private val songDao: SongDao
) {
    suspend fun getGenres(baseRequest: BaseRequest): BaseResponse<List<Genre>> {
        return apiService.getGenres(artistId = baseRequest.artistId ?: "",
            songId = baseRequest.songId ?: "")
    }


    suspend fun getBanners(): List<Banner> {
        val response = apiService.getBanners(name = "someName", value = "someValue")
        return response.data ?: emptyList()
    }

    suspend fun search(body: BaseRequest): BaseResponse<SearchResponse> {
        return apiService.search(body)
    }
    suspend fun listen(id:String, download :Boolean = false){
        apiService.listen(BaseRequest(songId = id, download = download))
    }

    suspend fun insertSong(song: Song) {
        return songDao.insertSong(song)
    }
}