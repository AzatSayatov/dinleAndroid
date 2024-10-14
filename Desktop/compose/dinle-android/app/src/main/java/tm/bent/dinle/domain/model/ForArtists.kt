package tm.bent.dinle.domain.model

data class Artists (
    val artistId: String,
    val nickname: String,
    val avatar: String,
    val id: String,
    val title: String
){

    fun getImage(): String {
        return avatar
    }
}