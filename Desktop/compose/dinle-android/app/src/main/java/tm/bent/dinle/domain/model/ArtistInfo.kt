package tm.bent.dinle.domain.model


data class ArtistInfo(
    val id : String,
    val title : String,
    val cover : String,
    val following: Boolean = false,
    val count: Count,
    val about: String?,
    val createdAt: String,
    val genres: List<Genre>,
) {

    fun getGenres(): String {
        return genres.joinToString { it.name }
    }


    fun getImage(): String {
        return cover
    }
}
