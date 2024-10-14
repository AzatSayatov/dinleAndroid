package tm.bent.dinle.ui.destinations.videoplayer

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.PowerManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun VideoPlayerScreen(
    url:String,
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val videoViewModel: VideoPlayerViewModel = hiltViewModel()
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "YourApp::VideoWakeLock")

    DisposableEffect(context) {
        wakeLock.acquire()
        onDispose {
            wakeLock.release()
        }
    }


    LaunchedEffect(url) {
        videoViewModel.playVideo(url)
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = videoViewModel.getPlayer() // Устанавливаем плеер
            }
        },
        update = {
            it.player = videoViewModel.getPlayer()
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }
                Lifecycle.Event.ON_RESUME,Lifecycle.Event.ON_START -> {
                    it.onResume()
                }
                else -> Unit
            }
        },
        modifier = Modifier
            .fillMaxWidth()
//            .aspectRatio(16 / 9f)
    )
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}