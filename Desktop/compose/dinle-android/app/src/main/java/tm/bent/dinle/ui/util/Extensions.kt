package tm.bent.dinle.ui.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.MyPlayer
import tm.bent.dinle.player.PlaybackState
import tm.bent.dinle.player.PlayerStates
import java.util.Collections


fun MutableList<Song>.resetTracks() {
    this.forEach { track ->
        track.isSelected = false
        track.state = PlayerStates.STATE_IDLE
    }
}


fun List<Song>.toMediaItemList(): MutableList<MediaItem> {


    return this.map {

        val mediaMetaData = MediaMetadata.Builder()
            .setArtworkUri(Uri.parse(it.getImage()))
            .setTitle(it.title)
            .setDescription(it.description)
            .setAlbumArtist(it.description)
            .build()

        val trackUri = Uri.parse(it.getSongUrl())
        MediaItem.Builder()
            .setUri(trackUri)
            .setMediaId(it.id)
            .setMediaMetadata(mediaMetaData)
            .build()

    }.toMutableList()
}





fun CoroutineScope.collectPlayerState(
    myPlayer: MyPlayer, updateState: (PlayerStates) -> Unit
) {

    this.launch {
        myPlayer.playerState.collect {
            updateState(it)
        }
    }
}


fun CoroutineScope.launchPlaybackStateJob(
    playbackStateFlow: MutableStateFlow<PlaybackState>, state: PlayerStates, myPlayer: MyPlayer
) = launch {

    do {
        playbackStateFlow.emit(
            PlaybackState(
                currentPlaybackPosition = myPlayer.currentPlaybackPosition,
                currentTrackDuration = myPlayer.currentTrackDuration
            )
        )
        delay(1000) // delay for 1 second
    } while (state == PlayerStates.STATE_PLAYING && isActive)
}


fun Long.formatTime(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val remainingSeconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun Long.formatDuration(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

private val VerticalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(x = 0f)
    override suspend fun onPreFling(available: Velocity) = available.copy(x = 0f)
}

private val HorizontalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(y = 0f)
    override suspend fun onPreFling(available: Velocity) = available.copy(y = 0f)
}

fun Modifier.disabledVerticalPointerInputScroll(disabled: Boolean = true) =
    if (disabled) this.nestedScroll(VerticalScrollConsumer) else this

fun Modifier.disabledHorizontalPointerInputScroll(disabled: Boolean = true) =
    if (disabled) this.nestedScroll(HorizontalScrollConsumer) else this

fun Bundle.readable() = buildList {
    keySet().forEach {
        add("key=$it, value=${get(it)}")
    }
}.joinToString()


fun isOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun <T> Context.systemService(name: String): T {
    return getSystemService(name) as T
}
fun <T> List<T>.swap(fromIdx: Int, toIdx: Int): List<T> {
    val copy = toMutableList()
    Collections.swap(copy, fromIdx, toIdx)
    return copy
}

fun <T> MutableList<T>.swap(fromIdx: Int, toIdx: Int) {
    Collections.swap(this, fromIdx, toIdx)
}


fun Modifier.clickWithoutIndication(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = MutableInteractionSource(),
        indication = null
    ) { onClick() }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun restartActivity(activity: Activity?) {
    val intent = activity?.getIntent()
    activity?.finish()
    activity?.startActivity(intent)
}
