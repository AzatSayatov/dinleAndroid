package tm.bent.dinle.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import tm.bent.dinle.data.remote.paging.SongsPagingSource
import tm.bent.dinle.data.remote.service.ApiService
import tm.bent.dinle.domain.dao.SongDao
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.Playlist
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.SongInfo
import java.io.File
import javax.inject.Inject


class SongRepository @Inject constructor(
    private val apiService: ApiService,
    private val songDao: SongDao
    ) {
    fun getSongs(baseRequest: BaseRequest) = Pager(
        PagingConfig(
            DEFAULT_PAGE_SIZE,
            prefetchDistance = 1
        )
    ){
        SongsPagingSource(
            baseRequest = baseRequest,
            apiService = apiService,
        )
    }.flow


    suspend fun getLastSeenSongs(): List<Song>? {
        return try {
            val response = apiService.fetchSongs()
            if (response.success) {
                response.data
            } else {
                // Handle the error case
                null
            }
        } catch (e: Exception) {
            // Handle the exception case
            null
        }
    }

    suspend fun likeSong(id:String){
        apiService.likeSong(id = id)
    }
//    suspend fun getSongsForPlaylist(playlistId: String): List<Song> {
//        val response = apiService.getSongsForPlaylist(playlistId)
//        return response.data?.map {
//            Song(
//                title = it.title,
//                cover = it.cover,
//                id = it.id
//            )
//        } ?: emptyList()
//    }

    suspend fun getSongInfo(id: String): BaseResponse<SongInfo> {
        return apiService.getSongInfo(id = id)
    }

    suspend fun likePlaylist(id:String){
        apiService.likePlaylist(id = id)
    }


    suspend fun updateSongsOrder(newSongsList: List<Song>) {
        // Предполагается, что у вас есть метод в SongDao для обновления списка песен
        songDao.updateSongsOrder(newSongsList)
    }

    suspend fun likeAlbum(id:String){
        apiService.likeAlbum(id = id)
    }

    suspend fun getPlaylist(id:String): BaseResponse<Playlist> {
        return apiService.getPlaylist(id = id)
    }

    suspend fun getAlbum(id:String): BaseResponse<Playlist> {
        return apiService.getAlbum(id = id)
    }



    suspend fun listen(id:String, download :Boolean = false){
//        Log.d("SongRepository", "Sending listen request for song ID: $id")
        apiService.listen(BaseRequest(songId = id, download = download))
    }

    suspend fun listenedSong(id:String){
//        Log.d("SongRepository", "Sending listen request for song ID: $id")
        apiService.listenedSong(BaseRequest(songId = id))
    }

    suspend fun sendAudio(file: File): BaseResponse<List<Song>>{
        val body = MultipartBody.Part.createFormData(
            "fileUrl", file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        return apiService.sendAudio(body)
    }


    suspend fun deleteSongById(songId: String) {
        songDao.deleteSongById(songId)
    }

    suspend fun insertSong(song: Song) {
        return songDao.insertSong(song)
    }

    fun getSongs(): Flow<MutableList<Song>> {
        return songDao.getSongs()
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 1000
    }
}