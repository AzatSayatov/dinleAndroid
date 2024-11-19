package tm.bent.dinle.domain.model

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.room.Entity
import androidx.room.PrimaryKey
import tm.bent.dinle.player.PlayerStates
import javax.annotation.concurrent.Immutable

@Entity
@Immutable
data class Song(
    @PrimaryKey val id : String,
    val title : String,
    val description : String,
    val cover : String,
    val link : String,
    val lyrics: String,
    var isLiked: Boolean,
    var artistId: String,
    var isSelected: Boolean = false,
    var isDownloaded: Boolean = false,
    var state: PlayerStates? = PlayerStates.STATE_IDLE,
    ){

    fun getImage(): String {
        return cover
    }

    fun getSongUrl(): String {
        return link
    }

    fun getDownloadUrl(): String {
        return link
    }

    fun isPlaying(): Boolean {
        return state == PlayerStates.STATE_READY
                || state == PlayerStates.STATE_BUFFERING
                || state == PlayerStates.STATE_PLAYING


    }

    fun toMediaItem(): MediaItem {
        val mediaMetaData = MediaMetadata.Builder()
            .setArtworkUri(Uri.parse(getImage()))
            .setTitle(title)
            .setDescription(description)
            .setAlbumArtist(description)
            .build()

        val trackUri = Uri.parse(getSongUrl())
        return MediaItem.Builder()
            .setUri(trackUri)
            .setMediaId(id)
            .setMediaMetadata(mediaMetaData)
            .build()
    }
    fun toDownloadMediaItem(): MediaItem {
        val mediaMetaData = MediaMetadata.Builder()
            .setArtworkUri(Uri.parse(getImage()))
            .setTitle(title)
            .setDescription(description)
            .setAlbumArtist(description)
            .build()

        val trackUri = Uri.parse(getDownloadUrl())
        return MediaItem.Builder()
            .setUri(trackUri)
            .setMediaId(id)
            .setMediaMetadata(mediaMetaData)
            .build()
    }

}


val mockSong = Song(
    id = "uuuuuu",
    title = "title",
    description = "description",
    cover = "",
    link = "",
    lyrics = "",
    isLiked = false,
    artistId = ""
)