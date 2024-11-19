package tm.bent.dinle.ui.destinations.shazam

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.ui.components.CustomIconButton
import tm.bent.dinle.ui.theme.Black80
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.InactiveBorder
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Surface
import tm.bent.dinle.ui.util.recorder.AndroidAudioRecorder
import java.io.File

@Destination
@Composable
fun ShazamScreen(
    navigator: DestinationsNavigator
) {

    val shazamViewModel = hiltViewModel<ShazamViewModel>()
//
    val uiState by shazamViewModel.uiState.collectAsState()

    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )

    val recorder by lazy {
        AndroidAudioRecorder(context)
    }
    var recording by rememberSaveable { mutableStateOf(false) }


    var audioFile: File? = null

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.equalizer_shazam)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            CoroutineScope(Dispatchers.IO).launch {
                File(context.cacheDir, "audio.mp3").also {
                    recording = true
                    recorder.start(it)
                    audioFile = it
                }
                delay(15000)
                recording = false
                recorder.stop()
                shazamViewModel.sendAudio(audioFile!!)
            }
        } else {

        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.isFailure) {
        if (uiState.isFailure) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Not found"
                )
            }

            shazamViewModel.updateToDefault()
        }

    }

    LaunchedEffect(context) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) -> {
                // Some works that require permission
                File(context.cacheDir, "audio.mp3").also {
                    recording = true
                    recorder.start(it)
                    audioFile = it
                }
                delay(15000)
                recording = false
                recorder.stop()
                shazamViewModel.sendAudio(audioFile!!)
            }

            else -> {
                // Asking for permission
                launcher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Surface, contentColor = Color.Red, snackbarData = data
                )
            }
        },
        topBar = {
            ShazamTopAppBar(
                title = ""
            ) {
                navigator.navigateUp()
            }
        },
    ) { padding ->

        if (uiState.data != null) {

            SongInfoView(
                song = uiState.data!!,
                onPlay = { song ->
                    val songs = listOf(song)
                    shazamViewModel.getPlayerController().init(
                        0, listOf(song)
                    )
                }
            )
        } else {


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier.padding(horizontal = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        modifier = Modifier.padding(bottom = 60.dp),
                        text = stringResource(id = R.string.app_name),
                        style = TextStyle(
                            fontSize = 26.sp,
                            lineHeight = 31.2.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(700),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Start

                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            .padding(bottom = 50.dp), contentAlignment = Alignment.Center
                    ) {

                        Spacer(modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                transformOrigin = TransformOrigin.Center
                            }
                            .size(120.0.dp)
                            .border(20.dp, InactiveBorder, CircleShape)
                            .padding(10.dp)
                            .border(10.dp, InactiveBorder, CircleShape)
                            .padding(10.dp)
                            .border(20.dp, InactiveBorder, CircleShape)
                            .padding(10.dp)
                            .border(20.dp, InactiveBorder, CircleShape)

                        )

                        Image(
                            modifier = Modifier
                                .size(65.dp)
                                .clip(CircleShape),
                            painter = painterResource(id = R.drawable.img_logo_big),
                            contentScale = ContentScale.Crop,
                            contentDescription = "",
                            alignment = Alignment.Center,
                        )
                    }

                    if (recording || uiState.isLoading) {
                        LottieAnimation(
                            composition,
                            progress,
                            modifier = Modifier
                                .size(35.dp, 28.dp),
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = stringResource(R.string.dinle_shazam_desc),
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(400),
                            color = Inactive,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

    }
}


@Composable
fun SongInfoView(
    song: Song,
    onPlay : (Song) -> Unit
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()

            .background(color = MaterialTheme.colorScheme.background),

        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 100.dp)
                .background(Inactive),
            model = song.cover,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black80)
                .blur(radius = 10.dp)
        )

        SubcomposeAsyncImage(
            modifier = Modifier
                .width(235.dp)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .background(Inactive),
            model = song.cover,
            contentDescription = null,
            contentScale = ContentScale.Crop,

            )

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp, bottom = 20.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 15.dp),
                    text = song.title ?: "",
                    style = TextStyle(
                        color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(600)
                    ),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = song.description ?: "",
                    style = TextStyle(
                        color = Inactive, fontSize = 14.sp, fontWeight = FontWeight(400)
                    ),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            CustomIconButton(
                modifier = Modifier.padding(end = 20.dp),
                icon = R.drawable.ic_play,
                color = MaterialTheme.colorScheme.onBackground
            ) {
                onPlay(song)
            }
        }

    }


}

@Composable
fun ShazamTopAppBar(
    title: String,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        if (leadingIcon != null) {
            leadingIcon()
        } else {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "logo",
                    tint = Color.White,

                    )
            }
        }

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(700),
                color = Color(0xFFFCFCFC),

                )
        )

        if (trailingIcon != null) {
            trailingIcon()
        }

    }
}