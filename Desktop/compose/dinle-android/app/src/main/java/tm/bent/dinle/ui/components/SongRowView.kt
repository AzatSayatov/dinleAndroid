package tm.bent.dinle.ui.components

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download.STATE_COMPLETED
import androidx.media3.exoplayer.offline.Download.STATE_DOWNLOADING
import androidx.media3.exoplayer.offline.Download.STATE_FAILED
import androidx.media3.exoplayer.offline.Download.STATE_QUEUED
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import tm.bent.dinle.R
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.destinations.downloads.DownloadsViewModel
import tm.bent.dinle.ui.theme.Primary1
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Subtitle


@OptIn(UnstableApi::class)
@Composable
fun SongRowView(
    song: Song,
    playerController: PlayerController,
    downloadTracker: DownloadTracker? = null,
    isLikeable: Boolean = false,
    extendable: Boolean = true,
    onLike: (Boolean) -> Unit = {},
    onDownload: () -> Unit = {},
    onMoreClick: () -> Unit,
    onClick: () -> Unit,
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.equalizer)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
//    STATE_QUEUED,
//    STATE_STOPPED,
//    STATE_DOWNLOADING,
//    STATE_COMPLETED,
//    STATE_FAILED,
//    STATE_REMOVING,
//    STATE_RESTARTING
    val state = downloadTracker?.getStatus(song.toMediaItem())
    var downloadProgress = downloadTracker?.getDownloadProgress(song.toMediaItem())?.percentDownloaded ?: 0f
    var isLiked by rememberSaveable {
        mutableStateOf(
            song.isLiked
        )
    }

    val songViewModel = hiltViewModel<DownloadsViewModel>()
    val songs by songViewModel.getDownloadedSongs().collectAsState(initial = emptyList())

    val isInArray = songs.any { it.id == song.id }
    val downloadedIcon = if(isInArray) R.drawable.ic_check_circled else R.drawable.ic_download_circle

    val likeIcon = if (isLiked) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(start = 20.dp, top = 8.dp, bottom = 8.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Box {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .aspectRatio(1f),
                model = song.getImage(),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                alignment = Alignment.Center,
                loading = {
                    GreyShimmer { brush ->
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush)
                        )
                    }
                },
                error = {
                    GreyShimmer { brush ->
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush)
                        )
                    }
                },
            )
        }

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {

            Row(verticalAlignment = Alignment.Bottom) {
                if (playerController.selectedTrack?.id == song.id) {
                    LottieAnimation(
                        composition,
                        progress,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(24.dp)
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(), text = song.title, style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 19.2.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(600),
                        color = MaterialTheme.colorScheme.onBackground,

                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }


            Text(
                modifier = Modifier.fillMaxWidth(), text = song.description, style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.2.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(400),
                    color = Subtitle,

                    ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            if (isLikeable){
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    onClick = {
                        isLiked = !isLiked
                        onLike(isLiked)
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                        painter = painterResource(id = likeIcon),
                        contentDescription = null
                    )
                }
            }else{


            Box {
                when (state) {


                    STATE_FAILED -> {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            onClick = {
                                downloadTracker?.download(song.toDownloadMediaItem())},
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified,
                                painter = painterResource(id = R.drawable.ic_retry),
                                contentDescription = null
                            )
                        }
                    }

                    STATE_QUEUED -> {
                        Icon(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = R.drawable.ic_queued),
                            contentDescription = null
                        )
                    }

                    STATE_COMPLETED -> {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            onClick = {
                                if (!isInArray){
                                    onDownload()
                                    downloadTracker?.download(song.toDownloadMediaItem())
                                }

                            },
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(7.dp)
                                    .size(24.dp),
                                tint = Color.Unspecified,
                                painter = painterResource(id = downloadedIcon),
                                contentDescription = null
                            )
                        }
                    }

                    STATE_DOWNLOADING -> {
                            Icon(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground,
                                painter = painterResource(id = R.drawable.ic_queued),
                                contentDescription = null
                            )
                    }
                    else -> {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            onClick = {
                                onDownload()
                                downloadTracker?.download(song.toDownloadMediaItem())
                                      },
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(7.dp)
                                    .size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground,
                                painter = painterResource(id = R.drawable.ic_download_circle),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            }
            if (extendable){
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    onClick = onMoreClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.ic_more_hr),
                        contentDescription = null
                    )
                }
            }

        }


    }
}



@OptIn(UnstableApi::class)
@Composable
fun DownloadSongRowView(
    song: Song,
    playerController: PlayerController,
    downloadTracker: DownloadTracker? = null,
    isLikeable: Boolean = false,
    extendable: Boolean = true,
    onLike: (Boolean) -> Unit = {},
    onDownload: () -> Unit = {},
    onMoreClick: () -> Unit,
    onClick: () -> Unit,
    deletingVisible: () -> Unit
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.equalizer)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
//    STATE_QUEUED,
//    STATE_STOPPED,
//    STATE_DOWNLOADING,
//    STATE_COMPLETED,
//    STATE_FAILED,
//    STATE_REMOVING,
//    STATE_RESTARTING
    val state = downloadTracker?.getStatus(song.toMediaItem())
    var downloadProgress = downloadTracker?.getDownloadProgress(song.toMediaItem())?.percentDownloaded ?: 0f
    var isLiked by rememberSaveable {
        mutableStateOf(
            song.isLiked
        )
    }

    val songViewModel = hiltViewModel<DownloadsViewModel>()
    val songs by songViewModel.getDownloadedSongs().collectAsState(initial = emptyList())

    val isInArray = songs.any { it.id == song.id }
    val downloadedIcon = if(isInArray) R.drawable.ic_check_circled else R.drawable.ic_download_circle


    val likeIcon = if (isLiked) R.drawable.ic_favorite_filled else R.drawable.ic_favorite
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(start = 20.dp, top = 8.dp, bottom = 8.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Box {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .aspectRatio(1f),
                model = song.getImage(),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                alignment = Alignment.Center,
                loading = {
                    GreyShimmer { brush ->
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush)
                        )
                    }
                },
                error = {
                    GreyShimmer { brush ->
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush)
                        )
                    }
                },
            )
        }

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {

            Row(verticalAlignment = Alignment.Bottom) {
                if (playerController.selectedTrack?.id == song.id) {
                    LottieAnimation(
                        composition,
                        progress,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(24.dp)
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(), text = song.title, style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 19.2.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(600),
                        color = MaterialTheme.colorScheme.onBackground,

                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }


            Text(
                modifier = Modifier.fillMaxWidth(), text = song.description, style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.2.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(400),
                    color = Subtitle,

                    ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            if (isLikeable){
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    onClick = {
                        isLiked = !isLiked
                        onLike(isLiked)
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                        painter = painterResource(id = likeIcon),
                        contentDescription = null
                    )
                }
            }else{


                Box {
                    when (state) {


                        STATE_FAILED -> {
                            IconButton(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.Transparent),
                                onClick = {
                                    downloadTracker?.download(song.toDownloadMediaItem())},
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified,
                                    painter = painterResource(id = R.drawable.ic_retry),
                                    contentDescription = null
                                )
                            }
                        }

                        STATE_QUEUED -> {
                            Icon(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground,
                                painter = painterResource(id = R.drawable.ic_queued),
                                contentDescription = null
                            )
                        }

                        STATE_COMPLETED -> {
                            IconButton(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.Transparent),
                                onClick = {
                                    if (!isInArray){
                                        onDownload()
                                        downloadTracker?.download(song.toDownloadMediaItem())
                                    }else{
                                        deletingVisible()
                                    }

                                },
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(7.dp)
                                        .size(24.dp),
                                    tint = Color.Unspecified,
                                    painter = painterResource(id = downloadedIcon),
                                    contentDescription = null
                                )
                            }
                        }

                        STATE_DOWNLOADING -> {
                            Icon(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground,
                                painter = painterResource(id = R.drawable.ic_queued),
                                contentDescription = null
                            )
                        }
                        else -> {
                            IconButton(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.Transparent),
                                onClick = {
                                    onDownload()
                                    downloadTracker?.download(song.toDownloadMediaItem())
                                },
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(7.dp)
                                        .size(24.dp),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    painter = painterResource(id = R.drawable.ic_download_circle),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }

            if (extendable){
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Transparent),
                    onClick = onMoreClick,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.ic_more_hr),
                        contentDescription = null
                    )
                }
            }

        }


    }
}


