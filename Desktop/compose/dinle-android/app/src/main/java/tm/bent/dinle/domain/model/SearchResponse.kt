package tm.bent.dinle.domain.model

data class SearchResponse(
    val alboms: List<Playlist>,
    val artists: List<Artist>,
    val songs: List<Song>,
    val shows: List<Media>,
    val playlists: List<Playlist>,
    val clips: List<Media>,

    ){

    fun isEmpty(): Boolean {
        return alboms.isEmpty()
                && artists.isEmpty()
                && songs.isEmpty()
                && shows.isEmpty()
                && playlists.isEmpty()
                && clips.isEmpty()

    }
}
