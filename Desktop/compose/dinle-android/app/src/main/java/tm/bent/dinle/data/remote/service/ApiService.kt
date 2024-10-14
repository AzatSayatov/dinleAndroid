package tm.bent.dinle.data.remote.service

import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import tm.bent.dinle.domain.model.Artist
import tm.bent.dinle.domain.model.ArtistDetail
import tm.bent.dinle.domain.model.ArtistInfo
import tm.bent.dinle.domain.model.Auth
import tm.bent.dinle.domain.model.Banner
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.BaseResponse
import tm.bent.dinle.domain.model.Device
import tm.bent.dinle.domain.model.DeviceResponse
import tm.bent.dinle.domain.model.FetchSong
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.domain.model.Home
import tm.bent.dinle.domain.model.ListenSong
import tm.bent.dinle.domain.model.Media
import tm.bent.dinle.domain.model.Message
import tm.bent.dinle.domain.model.News
import tm.bent.dinle.domain.model.PagingResponse
import tm.bent.dinle.domain.model.Playlist
import tm.bent.dinle.domain.model.SearchResponse
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.SongIn
import tm.bent.dinle.domain.model.SongInfo
import tm.bent.dinle.domain.model.SongRequest
import tm.bent.dinle.domain.model.SongResponce
import tm.bent.dinle.domain.model.StatusSong
import tm.bent.dinle.domain.model.TagList
import tm.bent.dinle.domain.model.Token
import tm.bent.dinle.domain.model.User


interface ApiService {

    @POST("auth/send-otp")
    suspend fun login(@Body body: Auth): BaseResponse<Message>

    @POST("auth/check-otp")
    suspend fun checkOtp(@Body body: Auth): retrofit2.Response<BaseResponse<User>>

    @POST("auth/log-out/{id}")
    suspend fun logout(@Path("id") id: String)

    @POST("auth/refresh")
    suspend fun refresh(@Body body: Token): BaseResponse<Token>

    @PATCH("client/mhome")
    suspend fun getPagingHome(@Body body: BaseRequest): PagingResponse<Home>

    @PATCH("client/home")
    suspend fun getHome(@Body body: BaseRequest): BaseResponse<List<Home>>

    @GET("client/banners")
    suspend fun getBanners(): BaseResponse<List<Banner>>

    @PATCH("client/alboms")
    suspend fun getAlbums(@Body body: BaseRequest): PagingResponse<Playlist>

    @PATCH("client/songs")
    suspend fun getSongs(@Body body: BaseRequest): PagingResponse<Song>

    @PATCH("client/playlists")
    suspend fun getPlaylists(@Body body: BaseRequest): PagingResponse<Playlist>

    @PATCH("artists")
    suspend fun getArtists(@Body body: BaseRequest): PagingResponse<Artist>

    @PATCH("playlist")
    suspend fun getMiksPLaylist(@Body requestBody: SongRequest): SongResponce

    @PATCH("playlist")
    suspend fun getTagList(@Body body: BaseRequest): PagingResponse<TagList>




    @PATCH("artists/one")
    suspend fun getArtistDetail(@Body body: BaseRequest): BaseResponse<ArtistDetail>

    @GET("artists/info/{id}")
    suspend fun getArtistInfo(@Path("id") id:String): BaseResponse<ArtistInfo>

    @POST("client/search")
    suspend fun search(@Body body: BaseRequest): BaseResponse<SearchResponse>

    @PATCH("client/genres")
    suspend fun getGenres(
        @Query("artistId") artistId: String,
        @Query("songId") songId: String
    ): BaseResponse<List<Genre>>

    @GET("auth/profile")
    suspend fun getProfile(): BaseResponse<User>

    @GET("client/favorite-songs")
    suspend fun getFavoriteSongs(@Query("page") id: Int): PagingResponse<Song>

    @GET("client/favorite-artists")
    suspend fun getFavoriteArtists(@Query("page") page: Int,@Query("pageSize") pageSize: Int): PagingResponse<Artist>

    @GET("client/favorite-playlists")
    suspend fun getFavoritePlaylist(@Query("page") id: Int): PagingResponse<Playlist>

    @GET("client/favorite-alboms")
    suspend fun getFavoriteAlbums(@Query("page") id: Int): PagingResponse<Playlist>

    @POST("artists/subscribe")
    suspend fun subscribe(@Body body: BaseRequest)

    @PATCH("client/song-like/{id}")
    suspend fun likeSong(@Path("id") id: String)

    @PATCH("client/playlist-like/{id}")
    suspend fun likePlaylist(@Path("id") id: String)

    @PATCH("client/albom-like/{id}")
    suspend fun likeAlbum(@Path("id") id: String)

    @GET("auth/devices")
    suspend fun getDevices(): BaseResponse<DeviceResponse>

    @POST("auth/remove-device/{id}")
    suspend fun removeDevice(@Path("id") id: String)

    @POST("auth/remove-deive-otp")
    suspend fun removeDeviceOtp(@Body body: Auth)

    @GET("client/songs/{id}")
    suspend fun getSongInfo(@Path("id") id: String): BaseResponse<SongInfo>

    @GET("client/banners?type=bildirish")
    suspend fun getBanners(@Query("name") name: String,@Query("value") value: String): BaseResponse<List<Banner>>

    @PATCH("client/shows")
    suspend fun getShows(@Body body: BaseRequest): PagingResponse<Media>

    @PATCH("client/clips")
    suspend fun getMedia(@Body body: BaseRequest): PagingResponse<Media>

    @GET("client/news/{id}")
    suspend fun getNewsDetail(@Path("id") id: String): BaseResponse<News>

    @PATCH("client/news")
    suspend fun getNewsList(@Body body: BaseRequest): PagingResponse<News>

    @Multipart
    @POST("artists/shazam")
    suspend fun sendAudio(
        @Part fileUrl: MultipartBody.Part,
    ):BaseResponse<List<Song>>

    @GET("client/playlist/one/{id}")
    suspend fun getPlaylist(@Path("id") id: String): BaseResponse<Playlist>

    @GET("client/albom/one/{id}")
    suspend fun getAlbum(@Path("id") id: String): BaseResponse<Playlist>

    @POST("auth/listen")
    suspend fun listen(@Body body: BaseRequest)

    @POST("auth/listen")
    suspend fun listenedSong(@Body body: BaseRequest)


    @GET("playlist/{id}/songs")
    suspend fun getSongsForPlaylist(@Path("id") playlistId: String): BaseResponse<List<Song>>


    @GET("auth/fetch-listens")
    suspend fun fetchSongs(): BaseResponse<List<Song>>

}