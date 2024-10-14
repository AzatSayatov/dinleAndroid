package tm.bent.dinle.ui.destinations.videoplayer

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(UnstableApi::class)
@Destination
@Composable
fun NewVideoScreen(
    videoUrls: List<String>,
    initialIndex: Int,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val videoViewModel: VideoPlayerViewModel = hiltViewModel()
    var currentIndex by remember { mutableStateOf(initialIndex) }

    // Добавляем все видео в ExoPlayer
    LaunchedEffect(videoUrls) {
        videoViewModel.getPlayer().apply {
            clearMediaItems()
            videoUrls.forEach { url ->
                addMediaItem(MediaItem.fromUri(url))
            }
            prepare()
            seekTo(currentIndex, 0L)
            playWhenReady = true
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).also { playerView ->
                playerView.player = videoViewModel.getPlayer()
                playerView.useController = true // Включаем контроллер плеера
                playerView.controllerShowTimeoutMs = 3000 // Тайм-аут скрытия контроллера
                playerView.showController()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    )

    BackHandler(true) {
        videoViewModel.getPlayer().stop()
        navigator.navigateUp()
    }
}
