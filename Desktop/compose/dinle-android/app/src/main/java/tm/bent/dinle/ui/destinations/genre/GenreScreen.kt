package tm.bent.dinle.ui.destinations.genre

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.mockGenre
import tm.bent.dinle.ui.components.DeletingDialog
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SongRowView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.components.toolbar.CollapsingToolbarScaffold
import tm.bent.dinle.ui.components.toolbar.ScrollStrategy
import tm.bent.dinle.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.downloads.DownloadsViewModel
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel
import tm.bent.dinle.ui.destinations.songs.SongsViewModel
import tm.bent.dinle.ui.theme.BackgroundGray
import tm.bent.dinle.ui.theme.Black80
import tm.bent.dinle.ui.theme.ButtonBackground
import tm.bent.dinle.ui.theme.Divider
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.util.ShareUtils

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun GenreScreen(
    genre: Genre = mockGenre,
    navigator: DestinationsNavigator
) {

    val playlistViewModel = hiltViewModel<PlaylistViewModel>()
    val songsViewModel = hiltViewModel<SongsViewModel>()

    val context = LocalContext.current

    LaunchedEffect(genre) {
        playlistViewModel.setBaseRequest(BaseRequest(genreId = genre.genreId))
    }

    val playerController = playlistViewModel.getPlayerController()
    val downloadTracker = playlistViewModel.getDownloadTracker()

    val songs = playlistViewModel.songs.collectAsLazyPagingItems()

    val downloadsViewModel = hiltViewModel<DownloadsViewModel>()
    val songsD by downloadsViewModel.getDownloadedSongs().collectAsState(initial = emptyList())
    val (isDeletingVisible, setDeletingVisible) = remember { mutableStateOf(false) }

    val state = rememberCollapsingToolbarScaffoldState()
    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }


    Scaffold { padding ->
        CollapsingToolbarScaffold(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallax(1f)
                        .background(color = MaterialTheme.colorScheme.background)
                        .alpha(state.toolbarState.progress),

                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.9f)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .blur(radius = 100.dp)
                                .aspectRatio(0.8f)
                                .graphicsLayer {
                                    alpha = state.toolbarState.progress
                                }
                                .background(Inactive),
                            model = genre.getImage(),
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
                            .background(Black80)
                            .blur(radius = 10.dp)
                    )


                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(top = 35.dp, bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .width(235.dp)
                                .aspectRatio(1f)
                                .graphicsLayer {
                                    alpha = state.toolbarState.progress
                                }
                                .clip(MaterialTheme.shapes.medium)
                                .background(Inactive),
                            model = genre.getImage(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,

                            )

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 15.dp),
                            text = genre.name,
                            style = TextStyle(
                                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(600)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = genre.name,
                            style = TextStyle(
                                color = Inactive, fontSize = 14.sp, fontWeight = FontWeight(400)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(

                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)

                        ) {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 10.dp)
                                    .background(BackgroundGray),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ButtonBackground
                                ),
                                onClick = {
                                    if (songs.itemSnapshotList.items.isNotEmpty()) {
                                        playerController.init(0, songs.itemSnapshotList.items)
                                    }
                                },

                                ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.White,
                                    painter = painterResource(id = R.drawable.ic_play),
                                    contentDescription = null
                                )
                            }

                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 10.dp)
                                    .background(BackgroundGray),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ButtonBackground
                                ),
                                onClick = {
                                    if (songs.itemSnapshotList.items.isNotEmpty()) {
                                        playerController.init(
                                            0,
                                            songs.itemSnapshotList.items.shuffled()
                                        )
                                    }
                                },

                                ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White,
                                    painter = painterResource(id = R.drawable.ic_shuffle),
                                    contentDescription = null
                                )
                            }
                        }
                    }


                }

                Row(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding(), bottom = 10.dp)
                        .fillMaxWidth()
                        .pin(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(modifier = Modifier

                        .padding(vertical = 10.dp), onClick = { navigator.navigateUp() }) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                    if (state.toolbarState.progress == 0f) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f),
                            text = genre.name,
                            style = TextStyle(
                                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(600)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (state.toolbarState.progress == 0f) {
                        FloatingActionButton(
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .size(44.dp)
                                .clip(shape = CircleShape),
                            shape = CircleShape,
                            onClick = { /*playerController::onPlayPauseClick*/ },
                            containerColor = Color.White.copy(0.05f),
                            contentColor = MaterialTheme.colorScheme.background,
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.ic_play),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
//                    else {
//                        IconButton(modifier = Modifier.padding(vertical = 8.dp), onClick = { }) {
//                            Icon(
//                                tint = Color.White,
//                                painter = painterResource(id = R.drawable.ic_more_vert),
//                                contentDescription = null
//                            )
//                        }
//                    }

                }
            }) {

            Box(
                contentAlignment = Alignment.TopCenter
            ) {
                if (songs.itemCount == 0 && songs.loadState.refresh == LoadState.Loading) {
                    LoadingView(Modifier.fillMaxWidth())
                } else if (songs.itemCount == 0 && songs.loadState.refresh is LoadState.Error) {
                    NoConnectionView(Modifier) {
                        songs.retry()
                        songs.refresh()
                    }
                } else if (songs.itemCount == 0) {
                    NotFoundView(Modifier.fillMaxSize())
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {

                        items(songs.itemCount) { index ->
                            val song = songs[index]!!

                            SongRowView(
                                song = song,
                                playerController = playerController,
                                onMoreClick = {
                                    selectedSong = song
                                    showMoreDialog = true
                                },
                                onDownload = {
                                    playlistViewModel.insertSong(song)
                                    playlistViewModel.listen(song.id, true)
                                },
                                downloadTracker = downloadTracker,
                                onClick = {
//                                    playerController.init(
//                                        song, songs.itemSnapshotList.items
//                                    )
                                    playerController.init(
                                        songs.itemSnapshotList.items.indexOf(song),
                                        songs.itemSnapshotList.items
                                    )
                                },
                                isDeletingVisible = {
                                    selectedSong = song
                                    setDeletingVisible(true)
                                }
                            )
                            HorizontalDivider(
                                color = Divider, modifier = Modifier.padding(start = 90.dp)
                            )
                        }

                    }
                }

            }

            if (isDeletingVisible) {
                DeletingDialog(title = stringResource(R.string.pozmak_isleyanizmi),
                    onConfirm = {
                        val indx = songsD.indexOf(selectedSong!!)
                        Log.e("myLog", "SongRowView: $indx")
                        downloadsViewModel.removeSongAt(indx)
                        setDeletingVisible(false)
                        selectedSong!!.isPlaying() == false
                    },
                    onDismissRequest = {
                        setDeletingVisible(false)
                    })
            }

            if (showMoreDialog && selectedSong != null) {
                SongActionsBottomSheet(song = selectedSong!!,
                    sheetState = moreDialogState,
                    onShare = {
                        ShareUtils.shareLink(context = context, link = SHARE_SONG_URL)
                    },
                    downloadTracker = downloadTracker,
                    playerController = playerController,
                    onNavigateToInfo = {
                        selectedSong?.id?.let { navigator.navigate(SongInfoScreenDestination(id = it)) }
                    },
                    onDownload = {
                        selectedSong?.let { song ->
                            playlistViewModel.listen(song.id, true)
                            playlistViewModel.insertSong(song)

                        }
                    },
                    onDismissRequest = {
                        showMoreDialog = false
                    },
                    onLike = {
                        songsViewModel.likeSong(selectedSong!!.id)
                    },
                    onNavigateToArtist = {
                        navigator.navigate(ArtistScreenDestination(BaseRequest(artistId = selectedSong!!.artistId)))

                    })
            }
        }
    }
}

