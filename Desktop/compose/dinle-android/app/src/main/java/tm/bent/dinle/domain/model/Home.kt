package tm.bent.dinle.domain.model

import javax.annotation.concurrent.Immutable

@Immutable
data class Home(
    val id: String,
    val name: String,
    val type: String,
    val rows: List<HomeItem>,

){
    fun isAlbums(): Boolean {
        return type == "alboms"
    }

    fun isArtists(): Boolean {
        return type == "artists"
    }

    fun isMedia(): Boolean {
        return type == "clip"
    }
    fun isNews(): Boolean {
        return type == "news"
    }

    fun isShow(): Boolean {
        return type == "shows"
    }

    fun isBanner(): Boolean {
        return type == "banner"
    }

    fun isPlaylistArray(): Boolean {
        return type == "playlist array"
    }

    fun isPlaylist(): Boolean {
        return type == "playlist"
    }

    fun isTopPlaylist(): Boolean {
        return type == "top-playlist"
    }

    fun isTagList(): Boolean{
        return type == "tag-list"
    }

    fun isNewTMSongs(): Boolean{
        return type == "playlist-square"
    }

    fun isTopSongs(): Boolean{
        return type == "top-playlist-square"
    }

    fun isMiks(): Boolean{
        return type == "miks"
    }

    fun toAlbums(): List<Playlist> {
        return rows.map { it.toPlaylist() }
    }

    fun toArtists(): List<Artist> {
        return rows.map { it.toArtist() }
    }

    fun toSongList(): List<Song> {
        return rows.map { it.toSong() }
    }

    fun toBanners(): List<Banner> {
        return rows.map { it.toBanner() }
    }
    fun toTagList(): List<TagList>{
        return rows.map{it.toTagList()}
    }

    fun toPlaylist(): Playlist {
         return Playlist(
            id = id,
            title = name,
            cover = "cover"
        )
    }

    fun toPlaylistArray(): List<Playlist> {
        return rows.map { it.toPlaylist() }
    }
}

@Immutable
data class HomeItem(
    val id: String,
    val title: String,
    val description: String,
    val cover: String,
    val link: String,
    val isLiked: Boolean,
    val lyrics: String,
    val artistId: String = "",
    val duration: Long = 0L,
    val artists: List<Artists>,
    val playlistId: String,
    val createdAt: String,
    val song: Song?
    ) {


    fun toSong(): Song {
        return Song(
            id = id,
            title = title,
            description = description,
            cover = cover,
            link = link,
            isLiked = isLiked,
            lyrics = lyrics,
            artistId = artistId
        )
    }

    fun toPlaylist(): Playlist {
        return Playlist(
            id = id,
            title = title,
            cover = cover,
            description =  description,
            isLiked = isLiked,
        )
    }

//    fun toAlbum() {
//        Album(
//            id = id,
//            title = title,
//            cover = cover
//        )
//    }

    fun toMedia(): Media {
        return Media(
            id = id,
            title = title,
            cover = cover,
            link = link,
            duration = duration,
        )
    }

    fun toShow(): Media {
        return Media(
            id = id,
            title = title,
            cover = cover,
            link = "",
            duration = 0L,
        )
    }

    fun toTagList(): TagList{
        return TagList(
            title = title,
            cover = cover,
            duration = 0,
            id = id
        )
    }

    fun toNews(): News {
        return News(
            id = id,
            title = title,
            cover = cover,
            viewed = 0
        )
    }

    fun toBanner(): Banner {
        return Banner(
            id = id,
            cover = cover,
            link = link,
            song = song
        )
    }

    fun toArtist(): Artist {
        return Artist(
            id = id,
            title = title,
            cover = cover
        )
    }
}
