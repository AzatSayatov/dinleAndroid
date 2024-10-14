package tm.bent.dinle.domain.model

data class TagList (
    val title: String,
    val cover: String,
    val duration: Int,
    val id: String
){

    fun getImage(): String {
        return cover
    }

}