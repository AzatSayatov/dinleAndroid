package tm.bent.dinle.domain.model

data class Banner(
    val id: String,
    val link: String?,
    val cover: String,
    val song: Song?
) {
}
