package tm.bent.dinle.domain.model

data class NewTMSongs(
    val playlistId: String,
    val createdAt: String,
    val artists: List<Artists>
) {

}