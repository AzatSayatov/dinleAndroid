package tm.bent.dinle.domain.model

data class SongInfo(
    val id: String,
    val title: String,
    val about: String,
    val lyrics: String,
    val createdAt: String,
    val albomId: String,
    val artistId: String,
    val albom: Playlist,
    val artist: Artist,
    val genres: List<Genre>,
    val duets: List<Role>,
    val cover: String,
    val count: Count,
) {
    fun getGenres(): String {
        return genres.joinToString { it.name }
    }
}

