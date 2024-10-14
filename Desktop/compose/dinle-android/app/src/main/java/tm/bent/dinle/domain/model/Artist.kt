package tm.bent.dinle.domain.model

import javax.annotation.concurrent.Immutable


@Immutable
data class Artist(
    val id : String,
    val title : String,
    val cover : String,
    var following: Boolean = false,
    val count: Count = Count()
){
    fun getImage(): String {
        return cover
    }
}


val mockArtist = Artist(
    id = "uuuuuu",
    title = "",
    cover = "",
    following = false,
    count = Count()
)


