package tm.bent.dinle.ui.destinations.player

import android.media.metrics.PlaybackStateEvent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.R
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.DownloadTracker
import tm.bent.dinle.player.PlaybackState
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.components.DeletingDialog
import tm.bent.dinle.ui.components.SongRowView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.downloads.DownloadsViewModel
import tm.bent.dinle.ui.theme.Background
import tm.bent.dinle.ui.theme.BackgroundSecond
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.backgroundGradient
import tm.bent.dinle.ui.util.ShareUtils
import tm.bent.dinle.ui.util.clickWithoutIndication
import tm.bent.dinle.ui.util.formatTime
import tm.bent.dinle.ui.util.rememberFlowWithLifecycle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    playerController: PlayerController,
    downloadTracker: DownloadTracker,
    onLike: (String) -> Unit,
    onListen: (String) -> Unit,
    onDownload: (Song) -> Unit,
    onNavigateToArtist: (String) -> Unit,
    onNavigateToInfo: () -> Unit,
    onDismiss: () -> Unit
) {

    val navigator : DestinationsNavigator
    val context = LocalContext.current

    val playerBottomSheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val playbackStateValue by playerController.playbackState.collectAsState(
        initial = PlaybackState(0L, 0L)
    )

    val progressState by rememberFlowWithLifecycle(playerController.playbackState)
    var draggingProgress by remember { mutableStateOf<Float?>(null) }

    val currentMediaProgress = progressState.currentPlaybackPosition.toFloat()


    val shuffleEnabled by playerController.shuffleEnabled.collectAsState()
    val repeatMode by playerController.repeatMode.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }


    var showLyrics by rememberSaveable { mutableStateOf(false) }
    var showPlaylist by rememberSaveable { mutableStateOf(false) }

    val song = playerController.selectedTrack
    val state = downloadTracker.getStatus(song!!.toMediaItem())




    LaunchedEffect(playerController.selectedTrack) {
        playerController.selectedTrack?.id?.let { onListen(it) }
    }

    ModalBottomSheet(sheetState = playerBottomSheet,
        onDismissRequest = onDismiss,
        shape = RectangleShape,
        dragHandle = {}) {
        Scaffold(topBar = {
            PlayerTopBar(title = "", trailingIcon = {
                IconButton(onClick = {
                    selectedSong = playerController.selectedTrack
                    showMoreDialog = true
                }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_more_hr),
                        contentDescription = "logo",
                        tint = Color.White,

                        )
                }

            }, leadingIcon = {
                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arr_down),
                        contentDescription = "logo",
                        tint = Color.White,

                        )
                }
            }) {

            }
        }) { padding ->


            Column {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.9f),

                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.9f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent)
                            )
                        ))
                    {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BackgroundSecond)
                                .blur(radius = 100.dp)
                                .aspectRatio(0.9f)
                                .clickable {
                                    if (!song.artistId.isNullOrEmpty()) {
                                        onNavigateToArtist(song.artistId)
                                    }
                                },
                            model = playerController.selectedTrack?.getImage(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.5f)) // Настройте степень прозрачности
                        )
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundGradient)
                            .blur(radius = 10.dp)
                    )

                    if (!showLyrics && !showPlaylist) {
                        PlayerCoverView(playerController = playerController,
                            downloadTracker = downloadTracker,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.9f),
                            onLike = { isLiked ->
                                playerController.selectedTrack?.let { song ->
                                    onLike(song.id)
                                    song.isLiked = isLiked
                                }

                            },
                            onDownload = { song ->
                                onDownload(song)
                            },
                            onNavigateToArtist = { id ->
                                onNavigateToArtist(id)
                            })
                    }

                    if (showLyrics) {
                        GradientBackgroundView(
                            padding = padding, modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.9f)
                        ) {
                            SubtitleView(
                                lyrics = playerController.selectedTrack?.lyrics ?: "",
                                padding = padding
                            )
                        }
                    }

                    if (showPlaylist) {
                        GradientBackgroundView(
                            padding = padding, modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.9f)
                        ) {
                            CurrentPlaylistView(padding = padding,
                                playerController = playerController,
                                downloadTracker = downloadTracker,
                                onMoreClick = {},
                                onDownload = { song ->
                                    onDownload(song)
                                })
                        }

                    }


                }
                Spacer(modifier = Modifier.height(20.dp))

                Slider(value = draggingProgress ?: currentMediaProgress,
                    onValueChange = { value ->
                        if (value == playbackStateValue.currentTrackDuration.toFloat()-1f){
                            playerController.onNextClick()
                        }else{
                            draggingProgress = value
                        }
                    },
                    onValueChangeFinished = {
                        draggingProgress?.toLong()?.let {
                            playerController.onSeekBarPositionChanged(it)
                        }
                        draggingProgress = null
                    },
                    interactionSource = interactionSource,
                    valueRange = 0f..playbackStateValue.currentTrackDuration.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Inactive2,
                    ),
                    thumb = {
                        SliderDefaults.Thumb(
                            modifier = Modifier
                                .offset(x = 0.dp)
                                .padding(5.dp),
                            interactionSource = remember { MutableInteractionSource() },
                            thumbSize = DpSize(10.dp, 10.dp)
                        )
                    },
                    track = {
                        SliderDefaults.Track(modifier = Modifier
                            .height(3.dp)
                            .padding(0.dp)
                            .defaultMinSize(1.dp, 1.dp),
                            sliderState = it,
                            thumbTrackGapSize = 0.dp,
                            drawStopIndicator = {})
                    })

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = playbackStateValue.currentPlaybackPosition.formatTime(),
                        fontSize = 12.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Normal,
                        color = Inactive
                    )
                    Text(
                        modifier = Modifier,
                        text = playbackStateValue.currentTrackDuration.formatTime(),
                        fontSize = 12.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Normal,
                        color = Inactive
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(modifier = Modifier.weight(1f), onClick = {
                        playerController.toggleShuffle(song)
                    }) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.ic_shuffle),
                            contentDescription = null,
                            tint = if (shuffleEnabled) MaterialTheme.colorScheme.primary else Color.White
                        )
                    }
                    IconButton(modifier = Modifier.weight(1f), onClick = {
                        playerController.onPreviousClick()
                    }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.ic_skip_backward),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    FloatingActionButton(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(shape = CircleShape),
                        onClick = { playerController.onPlayPauseClick() },
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.background,
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            painter = if (playerController.selectedTrack?.isPlaying() == true) painterResource(
                                id = R.drawable.ic_pause
                            ) else painterResource(id = R.drawable.ic_play),
                            contentDescription = null,
                            tint = Background
                        )
                    }
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { playerController.onNextClick() }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.ic_skip_forward),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    val painter = if (repeatMode == REPEAT_MODE_ONE) R.drawable.ic_repeat_one
                    else R.drawable.ic_repeat

                    IconButton(modifier = Modifier.weight(1f), onClick = {
                        val newRepeatMode = when (repeatMode) {
                            REPEAT_MODE_OFF -> REPEAT_MODE_ALL
                            REPEAT_MODE_ALL -> REPEAT_MODE_ONE
                            REPEAT_MODE_ONE -> REPEAT_MODE_OFF
                            else -> REPEAT_MODE_OFF
                        }
                        playerController.setRepeatMode(newRepeatMode)
                    }) {
                        val painter = when (repeatMode) {
                            REPEAT_MODE_ONE -> R.drawable.ic_repeat_one
                            else -> R.drawable.ic_repeat
                        }

                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = painter),
                            contentDescription = null,
                            tint = if (repeatMode != REPEAT_MODE_OFF) MaterialTheme.colorScheme.primary else Color.White
                        )
                    }

                }
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        showPlaylist = !showPlaylist
                        if (showLyrics) showLyrics = false
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_playing_playlist),
                            contentDescription = null,
                            tint = if (showPlaylist) MaterialTheme.colorScheme.primary else Inactive
                        )
                    }

                    IconButton(onClick = {
                        if (!playerController.selectedTrack?.lyrics.isNullOrEmpty()) {
                            showLyrics = !showLyrics
                            if (showPlaylist) showPlaylist = false
                        }

                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_text),
                            contentDescription = null,
                            tint = if (showLyrics) MaterialTheme.colorScheme.primary else Inactive
                        )
                    }
                }
            }

        }
    }

    if (showMoreDialog && selectedSong != null) {
        SongActionsBottomSheet(song = selectedSong!!,
            sheetState = moreDialogState,
            onShare = {
                ShareUtils.shareLink(context = context, link = SHARE_SONG_URL)
            },
            downloadTracker = downloadTracker,
            playerController = playerController,
            onNavigateToInfo = onNavigateToInfo,
            onDownload = {
                selectedSong?.let { onDownload(it) }
            },
            onDismissRequest = {
                showMoreDialog = false
            },
            onLike = {},
            onNavigateToArtist = {
                if (!selectedSong!!.artistId.isNullOrEmpty()) {
                    onNavigateToArtist(selectedSong!!.artistId)
                }
            })
    }


}


@Composable
fun PlayerTopBar(
    title: String,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
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
            modifier = Modifier.weight(1f), text = title, style = TextStyle(
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

@Composable
fun SubtitleView(
    lyrics: String, padding: PaddingValues
) {

    Text(
        modifier = Modifier
            .padding(paddingValues = padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 30.dp, 30.dp)
            .fillMaxSize(), text = lyrics,
        style = TextStyle(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(600),
            color = Color(0xFFFCFCFC),
            textAlign = TextAlign.Center

        )
    )

}


@Composable
fun GradientBackgroundView(
    modifier: Modifier,
    padding: PaddingValues,
    content: (@Composable () -> Unit),
) {

    val brush = Brush.verticalGradient(
        colors = listOf(
            Background,
            Color.Transparent,
        )
    )

    val brush2 = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent, Background
        )
    )

    Box(
        modifier = modifier

    ) {

        content()

        Box(
            modifier = Modifier
                .height(padding.calculateTopPadding())
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Background)
        )

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(50.dp)
                .background(brush)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(50.dp)
                .background(brush2)
        )
    }
}


@Composable
fun CurrentPlaylistView(
    padding: PaddingValues, downloadTracker: DownloadTracker,
    playerController: PlayerController, onMoreClick: (Song) -> Unit, onDownload: (Song) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(paddingValues = padding),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {

        items(playerController.tracks) { song ->

            SongRowView(song = song, downloadTracker = downloadTracker,
                playerController = playerController,
                onMoreClick = { onMoreClick(song) }, onDownload = {
                    onDownload(song)
                }, extendable = false,
                onClick = { playerController.onTrackClick(song) }
            )

            HorizontalDivider(
                color = Color.White.copy(0.04f), modifier = Modifier.padding(start = 80.dp)

            )
        }

    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayerCoverView(
    playerController: PlayerController,
    downloadTracker: DownloadTracker,
    modifier: Modifier,
    onNavigateToArtist: (String) -> Unit,
    onLike: (Boolean) -> Unit,
    onDownload: (Song) -> Unit
) {

    var isLiked by rememberSaveable {
        mutableStateOf(
            playerController.selectedTrack?.isLiked ?: false
        )
    }

    LaunchedEffect(playerController.selectedTrack) {
        isLiked = playerController.selectedTrack?.isLiked ?: false
    }

    val likeIcon = if (isLiked) R.drawable.ic_favorite_filled else R.drawable.ic_favorite

    val song = playerController.selectedTrack!!
    val state = downloadTracker.getStatus(song.toMediaItem())

    val songViewModel = hiltViewModel<DownloadsViewModel>()
    val songs by songViewModel.getDownloadedSongs().collectAsState(initial = emptyList())
    val (isDeletingVisible, setDeletingVisible) = remember { mutableStateOf(false) }


    val isInArray = songs.any { it.id == song.id }
    val downloadedIcon = if(isInArray) R.drawable.ic_check_circled else R.drawable.ic_download_circle

    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .size(290.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .aspectRatio(1f),
            model = playerController.selectedTrack?.getImage(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            Surface(
                modifier = Modifier
                    .background(Color.Transparent),
                color = Color.Transparent
            ) {
                when (state) {
                    null -> {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            onClick = {
                                Log.e("TAG", "ghghghgh: " + song)
                                onDownload(song)
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

                    PlaybackStateEvent.STATE_FAILED -> {
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Transparent),
                            onClick = { downloadTracker?.download(song.toDownloadMediaItem()) },
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground,
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
                                Log.e("TAG", "ghghghgh: " + song)
                                if (!isInArray){
                                    onDownload(song)
                                    downloadTracker?.download(song.toDownloadMediaItem())
                                }else{
//                                    setDeletingVisible(true)
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
                }
            }

//            if (isDeletingVisible) {
//                DeletingDialog(title = "Pozmak isleyanizmi",
//                    onConfirm = {
//                        val indx = songs.indexOf(song)
//                        Log.e("myLog", "SongRowView: $indx")
//                        songViewModel.removeSongAt(indx)
//                        song.isPlaying() == false
//                        setDeletingVisible(false)
//                    },
//                    onDismissRequest = {
//                        setDeletingVisible(false)
//                    })
//            }



            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = playerController.selectedTrack?.title ?: "", style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,

                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    modifier = Modifier
                        .basicMarquee(
                            // Animate forever.
                            iterations = Int.MAX_VALUE,
                        )
                        .clickWithoutIndication {
                            if (!playerController.selectedTrack?.artistId.isNullOrEmpty()) {
                                onNavigateToArtist(playerController.selectedTrack?.artistId!!)
                            }
                        },
                    text = playerController.selectedTrack?.description ?: "", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = RobotoFlex, fontWeight = FontWeight.Medium, color = Inactive
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            IconButton(onClick = {
                isLiked = !isLiked
                onLike(isLiked)
            }) {
                Icon(
                    painter = painterResource(id = likeIcon),
                    contentDescription = "logo",
                    tint = Color.Unspecified
                )
            }


        }
    }


}
