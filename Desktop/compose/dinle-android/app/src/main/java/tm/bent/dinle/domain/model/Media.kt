package tm.bent.dinle.domain.model

import androidx.compose.runtime.Immutable


@Immutable
data class Media(
    val id : String,
    val title : String,
    val link : String,
    val cover : String,
    val duration: Long,
)


val mockMedia = Media(
    id = "uuuuuu",
    title = "title",
    cover = "",
    link = "",
    duration = 100L
)