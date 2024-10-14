package tm.bent.dinle.ui.components.bottomsheet

import android.util.Log
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.compose.rememberNavController
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
import tm.bent.dinle.ui.components.DeletingDialog
import tm.bent.dinle.ui.components.GreyShimmer
import tm.bent.dinle.ui.destinations.downloads.DownloadsViewModel
import tm.bent.dinle.ui.theme.Button1
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Subtitle


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongActionsBottomSheet(
    song: Song,
    playerController: PlayerController,
    downloadTracker: DownloadTracker,
    sheetState: SheetState,
    onNavigateToInfo: () -> Unit,
    onShare: () -> Unit,
    onDownload: () -> Unit,
    onDismissRequest: () -> Unit,
    onLike: (Boolean) -> Unit = {},
    onNavigateToArtist: () -> Unit,
//    onPlayClick: () -> Unit
) {

    val songViewModel = hiltViewModel<DownloadsViewModel>()
    val songs by songViewModel.getDownloadedSongs().collectAsState(initial = emptyList())

    val navController = rememberNavController()

    val (isDeletingVisible, setDeletingVisible) = remember { mutableStateOf(false) }
    val state = downloadTracker?.getStatus(song.toMediaItem())
    var isLiked by rememberSaveable {
        mutableStateOf(
            song.isLiked
        )
    }
    val likeIcon = if (isLiked) R.drawable.ic_favorite_filled else R.drawable.ic_favorite

    val isInArray = songs.any { it.id == song.id }
    val downloadedIcon = if(isInArray) R.drawable.ic_check_circled else R.drawable.ic_download_circle

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            SongBottom(
                song = song,
                playerController = playerController,
            ) {

            }

            Row {

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .width(104.dp)
                        .height(70.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp), // Установите радиус закругления
                    color = Button1,
                ) {
                    when (state) {


                        Download.STATE_FAILED -> {
                            IconButton(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.Transparent),
                                onClick = { downloadTracker?.download(song.toDownloadMediaItem()) },
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified,
                                    painter = painterResource(id = R.drawable.ic_retry),
                                    contentDescription = null
                                )
                            }
                        }

                        Download.STATE_QUEUED -> {
                            Icon(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground,
                                painter = painterResource(id = R.drawable.ic_queued),
                                contentDescription = null
                            )
                        }

                        Download.STATE_COMPLETED -> {
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
                                        .padding(10.dp)
                                        .size(24.dp),
                                    tint = Color.Unspecified,
                                    painter = painterResource(id = downloadedIcon),
                                    contentDescription = null
                                )
                            }
                        }

                        Download.STATE_DOWNLOADING -> {
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
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    painter = painterResource(id = R.drawable.ic_download_circle),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }



                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .width(104.dp)
                        .height(70.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp), // Установите радиус закругления
                    color = Button1,
                ) {
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
                }


                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .width(104.dp)
                        .height(70.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp), // Установите радиус закругления
                    color = Button1,
                ) {
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Transparent),
                        onClick = {
                            onShare()
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified,
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = null
                        )
                    }
                }
            }


            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
            ) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp), // Установите радиус закругления
                    color = Button1 // Цвет фона
                ) {
                    ActionRowView(
                        icon = R.drawable.to_next,
                        title = stringResource(R.string.to_next)
                    ) {
                        playerController.addSongAfterCurrent(song)
                        onDismissRequest()
                    }
                }


                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp), // Установите радиус закругления
                    color = Button1 // Цвет фона
                ) {
                    ActionRowView(
                        icon = R.drawable.ic_info,
                        title = stringResource(id = R.string.info)
                    ) {
                        onNavigateToInfo()
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = RoundedCornerShape(16.dp), // Установите радиус закругления
                    color = Button1 // Цвет фона
                ) {

                    ActionRowView(
                        icon = R.drawable.ic_account_circle,
                        title = song.description
                    ) {
                        onNavigateToArtist()
                    }
                }


//                if (isInArray) {
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(4.dp),
//                        shape = RoundedCornerShape(16.dp), // Установите радиус закругления
//                        color = Button1 // Цвет фона
//                    ) {
//                        ActionRowView(
//                            icon = R.drawable.ic_delete,
//                            title = "Delete"
//                        ) {
//
//                            setDeletingVisible(true)
//
//                        }
//                    }
//
//                }


                if (isDeletingVisible) {
                    DeletingDialog(title = "Pozmak isleyanizmi",
                        onConfirm = {
                            val indx = songs.indexOf(song)
                            Log.e("myLog", "SongRowView: $indx")
                            songViewModel.removeSongAt(indx)
                            onDismissRequest()
                            song.isPlaying() == false
                        },
                        onDismissRequest = {
                            setDeletingVisible(false)
                        })
                }

            }

        }
    }
}

@Composable
fun ActionRowView(
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(shape = MaterialTheme.shapes.small)
        .clickable { onClick() }
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)) {

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = icon),
            contentDescription = null,

            )


        Text(
            modifier = Modifier.weight(weight = 1.0f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = title,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.onBackground,
            ),

            )

    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun SongBottom(
    song: Song,
    playerController: PlayerController,
    onPlayClick: () -> Unit
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.equalizer)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { }
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

        Row {
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

            IconButton(
                onClick = {
                    if (song.isPlaying()) {
                        playerController.onPlayPauseClick()

                    } else {
                        playerController.init(0, listOf(song))
                    }
                },
            ) {
                if (song.isPlaying()) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(
                            id = R.drawable.ic_pause
                        ),
                        contentDescription = "Play",
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(
                            id = R.drawable.ic_play
                        ),
                        contentDescription = "Play",

                        )
                }


            }
        }
    }
}

@Composable
fun ActionColumnView(
    icon: Int,
    onClick: () -> Unit,
    modofier: Modifier
) {
    Box(modifier = Modifier
        .width(114.dp)
        .clickable { onClick() }
        .padding(10.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}