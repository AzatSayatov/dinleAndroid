package tm.bent.dinle.ui.destinations.searchresults

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import tm.bent.dinle.domain.model.mockMedia
import tm.bent.dinle.ui.components.ArtistRowView
import tm.bent.dinle.ui.components.HeaderView
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.MediaRowView
import tm.bent.dinle.ui.components.MediaView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.PlaylistView
import tm.bent.dinle.ui.components.SongRowView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.components.search.SearchBar
import tm.bent.dinle.ui.destinations.AlbumsScreenDestination
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.ArtistsScreenDestination
import tm.bent.dinle.ui.destinations.MediasScreenDestination
import tm.bent.dinle.ui.destinations.PlaylistScreenDestination
import tm.bent.dinle.ui.destinations.PlaylistsScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.SongsScreenDestination
import tm.bent.dinle.ui.destinations.VideoPlayerScreenDestination
import tm.bent.dinle.ui.destinations.albums.SearchAlbums
import tm.bent.dinle.ui.destinations.artists.SearchArtists
import tm.bent.dinle.ui.destinations.medias.MediaViewModel
import tm.bent.dinle.ui.destinations.medias.SearchMedia
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel
import tm.bent.dinle.ui.destinations.playlists.SearchPlaylists
import tm.bent.dinle.ui.destinations.songs.SearchSong
import tm.bent.dinle.ui.destinations.songs.SongsScreen
import tm.bent.dinle.ui.destinations.songs.SongsViewModel
import tm.bent.dinle.ui.util.ShareUtils
import tm.bent.dinle.ui.util.clickWithoutIndication

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SearchResultsScreen(
    search: String,
    navigator: DestinationsNavigator
) {
    val searchBarString = search

    val searchViewModel = hiltViewModel<SearchViewModel>()
    val songViewModel = hiltViewModel<SongsViewModel>()
    val mediaViewModel = hiltViewModel<MediaViewModel>()


    val uiState by searchViewModel.uiState.collectAsState()
    val playerController = searchViewModel.getPlayerController()
    val downloadTracker = searchViewModel.getDownloadTracker()

    val context = LocalContext.current

    val baseRequest by searchViewModel.baseRequest.collectAsState(BaseRequest(search = search))

    LaunchedEffect(search) {
        if (baseRequest.search.isNullOrEmpty()) {
            searchViewModel.setBaseRequest(BaseRequest(search = search))

        }
    }
    val lazyListState = rememberLazyListState()


    val pullRefreshState =
        rememberPullRefreshState(refreshing = uiState.isLoading && uiState.data != null,
            onRefresh = {
                searchViewModel.setBaseRequest(baseRequest)
            })

    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    val tabs = listOf(
        stringResource(id = R.string.songs),
        stringResource(id = R.string.artists),
        stringResource(id = R.string.albums),
        stringResource(id = R.string.playlists),
        stringResource(id = R.string.media)
    )

    var selectedTab by rememberSaveable { mutableStateOf(0) }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(vertical = 10.dp)
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { searchBarString == "" }) {
                    Icon(
                        modifier = Modifier.clickWithoutIndication {
                            navigator.navigateUp()
                        },
                        tint = Color.White,
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null
                    )
                }




                SearchBar(
                    search = searchBarString,
                ) { searchStr ->
                    searchViewModel.setBaseRequest(BaseRequest(search = searchStr))
                }
            }

        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.TopCenter
        ) {
            if (uiState.isLoading) {
                searchViewModel.setBaseRequest(BaseRequest(search = search))
                LoadingView(Modifier.fillMaxSize())
            } else if (uiState.isFailure) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    searchViewModel.setBaseRequest(BaseRequest(search = search))
                }
            } else if (uiState.data?.isEmpty() == true) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                Column {



                    Column {
                        ScrollableTabRow(
                            selectedTabIndex = selectedTab,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.Black),
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            edgePadding = 0.dp,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = Color.White // Устанавливаем цвет индикатора на белый
                                )
                            }) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = { Text(text = title) },
                                )
                            }
                        }
                        when (selectedTab) {
                            0 -> {
                                SearchSong(baseRequest = BaseRequest(search = search), navigator = navigator)
                            }

                            1 -> {
                                SearchArtists(
                                    navigator = navigator,
                                    baseRequest = BaseRequest(search = search)
                                )
                            }

                            2 -> {
                                SearchAlbums(
                                    navigator = navigator,
                                    baseRequest = BaseRequest(search = search)
                                )
                            }

                            3 -> {
                                SearchPlaylists(
                                    navigator = navigator,
                                    baseRequest = BaseRequest(search = search)
                                )
                            }

                            4 -> {
//                                SearchMedia(navigator = navigator,
//                                    baseRequest = baseRequest){
//                                    navigator.navigate(VideoPlayerScreenDestination(url = mockMedia.link))
//                                }


                                val medias = mediaViewModel.medias.collectAsLazyPagingItems()



                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    if (medias.itemCount == 0 && medias.loadState.refresh == LoadState.Loading) {
                                        LoadingView(Modifier.fillMaxSize())
                                    } else if (medias.itemCount == 0 && medias.loadState.refresh is LoadState.Error) {
                                        NoConnectionView(Modifier.fillMaxSize()) {
                                            medias.retry()
                                            medias.refresh()
                                        }
                                    } else if (medias.itemCount == 0) {
                                        NotFoundView(Modifier.fillMaxSize())
                                    } else {
                                        LazyColumn(
                                            contentPadding = PaddingValues(vertical = 8.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {

                                            items(medias.itemCount) { index ->
                                                val show = medias[index]!!
                                                MediaRowView(
                                                    media = show,
                                                    onClick = {
                                                        navigator.navigate(
                                                            VideoPlayerScreenDestination(url = show.link)
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }


            }
            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                pullRefreshState,
            )

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
                            searchViewModel.listen(song.id, true)
                            searchViewModel.insertSong(song)

                        }
                    },
                    onDismissRequest = {
                        showMoreDialog = false
                    },
                    onLike = {},
                    onNavigateToArtist = {
                        navigator.navigate(ArtistScreenDestination(BaseRequest(artistId = selectedSong!!.artistId)))

                    })
            }
        }

    }
}


//                                        LazyColumn(
////                    verticalArrangement = Arrangement.spacedBy(20.dp),
//                        contentPadding = PaddingValues(vertical = 20.dp)
//                    ) {
//                        if (!uiState.data?.songs.isNullOrEmpty()) {
//                            item {
//                                Column {
//                                    HeaderView(stringResource(id = R.string.songs)) {
//                                        navigator.navigate(
//                                            SongsScreenDestination(
//                                                baseRequest = baseRequest
//                                            )
//                                        )
//                                    }
//                                    Spacer(modifier = Modifier.height(10.dp))
//
//                                }
//                            }
//                            items(uiState.data?.songs!!) { song ->
//                                SongRowView(
//                                    song = song,
//                                    playerController = playerController,
//                                    onMoreClick = {
//                                        selectedSong = song
//                                        showMoreDialog = true
//                                    },
//                                    onDownload = {
//                                        searchViewModel.insertSong(song)
//                                        searchViewModel.listen(song.id, true)
//                                    },
//                                    downloadTracker = downloadTracker,
//                                    onClick = {
//                                        uiState.data?.songs?.let {
//                                            playerController.init(
//                                                song, it
//                                            )
//                                        }
//
//                                    },
//                                )
//
//                            }
//                        }
//
//                        if (!uiState.data?.artists.isNullOrEmpty()) {
//
//                            item {
//                                Spacer(modifier = Modifier.height(10.dp))
//
//                                Column {
//                                    HeaderView(stringResource(id = R.string.artists)) {
//                                        navigator.navigate(
//                                            ArtistsScreenDestination(
//                                                baseRequest = BaseRequest(
//                                                    search = search
//                                                )
//                                            )
//                                        )
//
//                                    }
//                                }
//                            }
//                            items(uiState.data?.artists!!, key = { artist -> artist.id }) { artist ->
//                                ArtistRowView(
//                                    artist = artist
//                                ) {
//                                    navigator.navigate(
//                                        ArtistScreenDestination(
//                                            baseRequest = BaseRequest(
//                                                artistId = artist.id
//                                            )
//                                        )
//                                    )
//                                }
//                            }
//
//                        }
//                        if (!uiState.data?.alboms.isNullOrEmpty()) {
//
//                            item {
//                                Column {
//                                    Spacer(modifier = Modifier.height(10.dp))
//
//                                    HeaderView(stringResource(id = R.string.albums)) {
//                                        navigator.navigate(
//                                            AlbumsScreenDestination(
//                                                baseRequest = baseRequest
//                                            )
//                                        )
//                                    }
//
//                                    LazyRow(
//                                        contentPadding = PaddingValues(20.dp, 10.dp),
//                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
//                                    ) {
//                                        items(
//                                            uiState.data?.alboms!!,
//                                            key = { playlist -> playlist.id }) { playlist ->
//                                            PlaylistView(
//                                                playlist = playlist, hasFixedSize = true
//                                            ) {
//                                                navigator.navigate(
//                                                    PlaylistScreenDestination(
//                                                        PlaylistViewModel.TYPE_ALBUM, playlist
//                                                    )
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        if (!uiState.data?.playlists.isNullOrEmpty()) {
//                            item {
//
//                                Column {
//                                    Spacer(modifier = Modifier.height(10.dp))
//
//                                    HeaderView(stringResource(id = R.string.playlists)) {
//                                        navigator.navigate(
//                                            PlaylistsScreenDestination(
//                                                baseRequest = baseRequest
//                                            )
//                                        )
//                                    }
//
//                                    LazyRow(
//                                        contentPadding = PaddingValues(20.dp, 10.dp),
//                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
//                                    ) {
//                                        items(
//                                            uiState.data?.playlists!!,
//                                            key = { playlist -> playlist.id }) { playlist ->
//                                            PlaylistView(
//                                                playlist = playlist, hasFixedSize = true
//                                            ) {
//                                                navigator.navigate(
//                                                    PlaylistScreenDestination(
//                                                        PlaylistViewModel.TYPE_PLAYLIST, playlist
//                                                    )
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        if (!uiState.data?.clips.isNullOrEmpty()) {
//                            item {
//
//                                Column {
//                                    Spacer(modifier = Modifier.height(10.dp))
//                                    val title = stringResource(id = R.string.media)
//
//                                    HeaderView(title) {
//                                        navigator.navigate(
//                                            MediasScreenDestination(
//                                                title = title,
//                                                baseRequest = baseRequest
//                                            )
//                                        )
//                                    }
//
//                                    LazyRow(
//                                        contentPadding = PaddingValues(20.dp, 10.dp),
//                                        horizontalArrangement = Arrangement.spacedBy(20.dp)
//                                    ) {
//                                        items(
//                                            uiState.data?.clips!!,
//                                            key = { media -> media.id }) { media ->
//                                            MediaView(
//                                                media = media,
//                                                onMoreClick = {},
//                                                onClick = {
//                                                    navigator.navigate(
//                                                        VideoPlayerScreenDestination(url = media.link)
//                                                    )
//
//                                                }
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                    }

