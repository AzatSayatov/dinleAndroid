package tm.bent.dinle.domain.model

data class SongResponce(
    val count: Int,
    val duration: Int,
    val cover: String,
    val producedAt: String,
    val title: String,
    val isLiked: Boolean,
    val pageCount: Int,
    val rows: List<Song>
){
    fun getImage(): String{
        return cover
    }
}