package tm.bent.dinle.domain.model

data class SongRequest (
    val page: Int,
    val pageSize: Int,
    val search: String,
    val playlistId: String,
    val artistId: String,
    val albomId: String,
    val genreId: String
)