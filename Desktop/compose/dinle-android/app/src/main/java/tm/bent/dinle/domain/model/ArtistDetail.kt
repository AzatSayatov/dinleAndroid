package tm.bent.dinle.domain.model

data class ArtistDetail(
    val artist: Artist,
    val songs: List<Song>,
    val alboms: List<Playlist>,
    val clips: List<Media>,
)
