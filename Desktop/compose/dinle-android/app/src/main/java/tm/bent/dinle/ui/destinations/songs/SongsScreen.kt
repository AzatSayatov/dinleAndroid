package tm.bent.dinle.ui.destinations.songs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.R
import tm.bent.dinle.di.BASE_URL
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.SongRowView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.searchresults.SearchViewModel
import tm.bent.dinle.ui.util.ShareUtils

@OptIn(ExperimentalMaterial3Api::class)
@Destination()
@Composable
fun SongsScreen(
    title: String? = null,
    baseRequest: BaseRequest,
    navigator: DestinationsNavigator,

    ) {


    val songViewModel = hiltViewModel<SongsViewModel>()

    val context = LocalContext.current

    LaunchedEffect(baseRequest){
        songViewModel.setBaseRequest(baseRequest)
    }

    val playerController =  songViewModel.getPlayerController()
    val downloadTracker = songViewModel.getDownloadTracker()

    val songs = songViewModel.songs.collectAsLazyPagingItems()

    val isRefreshing = songs.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }

    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = songs.itemCount != 0 && songs.loadState.refresh == LoadState.Loading,
        onRefresh = {
            songs.retry()
            songs.refresh()
        }
    )

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
        topBar = {
            SimpleTopAppBar(title = title ?: stringResource(id = R.string.songs)) {
                navigator.navigateUp()
            }
        },
    ) { padding ->

        Box(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            if (songs.itemCount == 0 && songs.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (songs.itemCount == 0 && songs.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    songs.retry()
                    songs.refresh()
                }
            } else if (songs.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    state = lazyListState
                ) {

                    items(songs.itemCount) {index ->
                        val song = songs[index]!!

                        SongRowView(
                            song = song,
                            playerController = playerController,
                            onMoreClick = {
                                selectedSong = song
                                showMoreDialog = true
                            },
                            isLikeable = baseRequest.isLiked == true,
                            onLike = {
                                songViewModel.likeSong(song.id)
                            },
                            onDownload = {
                                songViewModel.insertSong(song)
                                songViewModel.listen(song.id, true)
                            },
                            downloadTracker = downloadTracker,
                            onClick = {
                                songViewModel.listenedSong(song.id)
//                                playerController.init(
//                                    song, songs.itemSnapshotList.items
//                                )
                                playerController.init(songs.itemSnapshotList.items.indexOf(song),songs.itemSnapshotList.items)
                            },
                        )
                    }

                }
            }
            PullRefreshIndicator(
                refreshing = songs.itemCount != 0 && songs.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )

            if (showMoreDialog && selectedSong != null) {
                SongActionsBottomSheet(
                    song = selectedSong!!,
                    sheetState = moreDialogState,
                    onShare = {
                        ShareUtils.shareLink(context = context, link = SHARE_SONG_URL)
                    },
                    downloadTracker = downloadTracker,
                    playerController = playerController,
                    onNavigateToInfo = {
                        selectedSong?.id?.let { navigator.navigate( SongInfoScreenDestination(id = it)) }
                    },
                    onDownload = {
                        selectedSong?.let {song ->
                            songViewModel.insertSong(song)
                            songViewModel.listen(song.id, true)
                        }
                    },
                    onDismissRequest = {
                        showMoreDialog = false
                    },
                    onLike = {},
                    onNavigateToArtist = {
                        navigator.navigate(ArtistScreenDestination(BaseRequest(artistId = selectedSong!!.artistId)))

                    }
                )
            }
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Destination()
@Composable
fun SearchSong(
    baseRequest: BaseRequest,
    navigator: DestinationsNavigator,

    ) {

    val searchViewModel = hiltViewModel<SearchViewModel>()
    val uiState by searchViewModel.uiState.collectAsState()


    val songViewModel = hiltViewModel<SongsViewModel>()

    val context = LocalContext.current

    LaunchedEffect(baseRequest){
        songViewModel.setBaseRequest(baseRequest)
    }

    val playerController =  songViewModel.getPlayerController()
    val downloadTracker = songViewModel.getDownloadTracker()

    val songs = songViewModel.songs.collectAsLazyPagingItems()

    val isRefreshing = songs.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }

    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = songs.itemCount != 0 && songs.loadState.refresh == LoadState.Loading,
        onRefresh = {
            songs.retry()
            songs.refresh()
        }
    )

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
    ) { padding ->

        Box(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            if (songs.itemCount == 0 && songs.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (songs.itemCount == 0 && songs.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    songs.retry()
                    songs.refresh()
                }
            } else if (songs.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    state = lazyListState
                ) {

                    items(songs.itemCount) {index ->
                        val song = songs[index]!!

                        SongRowView(
                            song = song,
                            playerController = playerController,
                            onMoreClick = {
                                selectedSong = song
                                showMoreDialog = true
                            },
                            isLikeable = baseRequest.isLiked == true,
                            onLike = {
                                songViewModel.likeSong(song.id)
                            },
                            onDownload = {
                                songViewModel.insertSong(song)
                                songViewModel.listen(song.id, true)
                            },
                            downloadTracker = downloadTracker,
                            onClick = {
                                songViewModel.listenedSong(song.id)
//                                playerController.init(
//                                    song, songs.itemSnapshotList.items
//                                )
                                playerController.init(songs.itemSnapshotList.items.indexOf(song),songs.itemSnapshotList.items)

                            },
                        )
                    }

                }
            }
            PullRefreshIndicator(
                refreshing = songs.itemCount != 0 && songs.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )

            if (showMoreDialog && selectedSong != null) {
                SongActionsBottomSheet(
                    song = selectedSong!!,
                    sheetState = moreDialogState,
                    onShare = {
                        ShareUtils.shareLink(context = context, link = SHARE_SONG_URL)
                    },
                    downloadTracker = downloadTracker,
                    playerController = playerController,
                    onNavigateToInfo = {
                        selectedSong?.id?.let { navigator.navigate( SongInfoScreenDestination(id = it)) }
                    },
                    onDownload = {
                        selectedSong?.let {song ->
                            songViewModel.insertSong(song)
                            songViewModel.listen(song.id, true)
                        }
                    },
                    onDismissRequest = {
                        showMoreDialog = false
                    },
                    onLike = {
                             songViewModel.likeSong(selectedSong!!.id)
                    },
                    onNavigateToArtist = {
                        navigator.navigate(ArtistScreenDestination(BaseRequest(artistId = selectedSong!!.artistId)))

                    }
                )
            }
        }

    }


}