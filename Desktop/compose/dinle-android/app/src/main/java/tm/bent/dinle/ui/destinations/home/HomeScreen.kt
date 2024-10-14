package tm.bent.dinle.ui.destinations.home


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import tm.bent.dinle.R
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.domain.model.SongResponce
import tm.bent.dinle.ui.components.ArtistView
import tm.bent.dinle.ui.components.HeaderView
import tm.bent.dinle.ui.components.LastListened
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.MediaView
import tm.bent.dinle.ui.components.MiksView
import tm.bent.dinle.ui.components.NewsView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.PlaylistView
import tm.bent.dinle.ui.components.ReadyPlaylist
import tm.bent.dinle.ui.components.SongView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.components.pager.HorizontalPagerView
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.AlbumsScreenDestination
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.ArtistsScreenDestination
import tm.bent.dinle.ui.destinations.MediasScreenDestination
import tm.bent.dinle.ui.destinations.NewsDetailScreenDestination
import tm.bent.dinle.ui.destinations.NewsListScreenDestination
import tm.bent.dinle.ui.destinations.PlaylistScreenDestination
import tm.bent.dinle.ui.destinations.PlaylistsScreenDestination
import tm.bent.dinle.ui.destinations.ProfileScreenDestination
import tm.bent.dinle.ui.destinations.ShowsScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.VideoPlayerScreenDestination
import tm.bent.dinle.ui.destinations.newsdetail.NewsDetailViewModel
import tm.bent.dinle.ui.destinations.player.SongItem
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel.Companion.TYPE_ALBUM
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel.Companion.TYPE_PLAYLIST
import tm.bent.dinle.ui.destinations.songs.SongsViewModel
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.WhiteMilk
import tm.bent.dinle.ui.util.ShareUtils
import tm.bent.dinle.ui.util.clickWithoutIndication

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@RootNavGraph(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
) {

    val context = LocalContext.current

    val brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xff090909), Color(0xff111111)
        )
    )

    val tm_top_str = stringResource(R.string.world_top_song)
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val newsDetailViewModel = hiltViewModel<NewsDetailViewModel>()
    val songsViewModel = hiltViewModel<SongsViewModel>()


    val playerController = homeViewModel.getPlayerController()
    val downloadTracker = homeViewModel.getDownloadTracker()

    val uiState by homeViewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.data?.isNotEmpty() == true && uiState.isLoading,
        onRefresh = { homeViewModel.getHome() }
    )


    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp - 40.dp
    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.main), style = TextStyle(
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                )
            }, colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            actions = {
                Icon(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(24.dp)
                        .clickWithoutIndication { navigator.navigate(ProfileScreenDestination) },
                    painter = painterResource(id = R.drawable.ic_account_circle),
                    contentDescription = null,
                    tint = WhiteMilk
                )
            }
        )
    })


    { padding ->

        Box(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .pullRefresh(pullRefreshState)
                .background(Color.Black), contentAlignment = Alignment.TopCenter

        ) {
            if (uiState.isLoading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (uiState.isFailure) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    homeViewModel.getHome()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {


                    uiState.data?.let { homeData ->
                        items(homeData, key = { homeItem -> homeItem.id }) { data ->

                            Column {
                                HeaderView(
                                    title = data.name,
                                    extendable = !data.isBanner() && !data.isTagList() && !data.isMiks(),
                                ) {
                                    when {
                                        data.isAlbums() -> {
                                            navigator.navigate(AlbumsScreenDestination())
                                        }

                                        data.isArtists() -> {
                                            navigator.navigate(ArtistsScreenDestination())
                                        }

                                        data.isPlaylist() -> {
                                            val playlist = data.toPlaylist()
                                            navigator.navigate(
                                                PlaylistScreenDestination(
                                                    TYPE_PLAYLIST, playlist
                                                )
                                            )
                                        }

                                        data.isTopPlaylist() -> {
                                            val playlist = data.toPlaylist()
                                            navigator.navigate(
                                                PlaylistScreenDestination(
                                                    TYPE_PLAYLIST, playlist
                                                )
                                            )
                                        }

                                        data.isNewTMSongs() -> {
                                            val playlist = data.toPlaylist()
                                            navigator.navigate(
                                                PlaylistScreenDestination(
                                                    TYPE_PLAYLIST, playlist
                                                )
                                            )
                                        }

                                        data.isTopSongs() -> {
                                            val playlist = data.toPlaylist()
                                            navigator.navigate(
                                                PlaylistScreenDestination(
                                                    TYPE_PLAYLIST, playlist
                                                )
                                            )
                                        }

                                        data.isShow() -> {
                                            navigator.navigate(
                                                ShowsScreenDestination(
                                                    baseRequest = BaseRequest()
                                                )
                                            )
                                        }

                                        data.isMedia() -> {
                                            navigator.navigate(
                                                MediasScreenDestination(
                                                    title = data.name,
                                                    baseRequest = BaseRequest(
                                                        clipId = data.id
                                                    )
                                                )
                                            )
                                        }


                                        data.isPlaylistArray() -> {
                                            navigator.navigate(PlaylistsScreenDestination())
                                        }

                                        data.isNews() -> {
                                            navigator.navigate(NewsListScreenDestination(BaseRequest()))
                                        }
                                    }
                                }
                                if (data.isBanner()) {

                                    val pagerState =
                                        rememberPagerState(0, pageCount = { data.rows.size })
                                    var pageKey by remember { mutableStateOf(0) }
                                    val songResponce: SongResponce

                                    LaunchedEffect(pageKey) {
                                        delay(3000)
                                        if (pagerState.pageCount <= 1) return@LaunchedEffect
                                        var newPosition = pagerState.currentPage + 1
                                        if (newPosition == data.rows.size) newPosition = 0
                                        pagerState.animateScrollToPage(newPosition)
                                        pageKey = newPosition
                                    }

                                    if (data.id == "banner") {
                                        HorizontalPagerView(
                                            banners = data.toBanners(),
                                            pagerState = pagerState,
                                            imageHeight = 230
                                        ){
                                            
                                        }
                                    } else {
                                        HorizontalPagerView(
                                            banners = data.toBanners(),
                                            pagerState = pagerState,
                                            imageHeight = 140
                                        ){

                                        }
                                    }


                                } else {
                                    LazyRow(
                                        Modifier
                                            .fillMaxWidth()
                                            .background(color = MaterialTheme.colorScheme.background)
                                            .defaultMinSize(minHeight = 50.dp),
                                        contentPadding = PaddingValues(7.dp, 7.dp),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
//                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        when {

                                            data.isTagList() -> {



                                                itemsIndexed(
                                                    data.rows,
                                                    key = { _, playlist -> playlist.id }) { index, playlist ->

                                                    val playlistViewModel = hiltViewModel<PlaylistViewModel>()

                                                    LaunchedEffect(playlist) {
                                                        val baseRequest = BaseRequest().apply {
                                                            playlistId = playlist.id
                                                        }
                                                        playlistViewModel.setBaseRequest(baseRequest)
                                                        playlistViewModel.setPlaylist(playlist.toPlaylist())
                                                        playlistViewModel.getPlaylist(playlist.id, TYPE_ALBUM)
                                                    }

                                                    val playerController = playlistViewModel.getPlayerController()
                                                    val songs = playlistViewModel.songs.collectAsLazyPagingItems()

                                                    ReadyPlaylist(
                                                        playlist = playlist.toPlaylist(),
                                                    ) {
                                                        if (songs.itemSnapshotList.items.isNotEmpty()) {
                                                            playerController.init(0, songs.itemSnapshotList.items)
                                                            playerController.originalTracks.addAll(songs.itemSnapshotList.items)
                                                        }
                                                    }
                                                }


//                                                items(data.rows,
//                                                    key = { playlist -> playlist.id }) { playlist ->
//
//                                                    val songs =
//                                                        playlistViewModel.songs.collectAsLazyPagingItems()
//                                                    LaunchedEffect(playlist) {
//                                                        val baseRequest = BaseRequest().apply {
//                                                             playlistId = playlist.id
//                                                        }
//                                                        playlistViewModel.setBaseRequest(baseRequest)
//                                                        playlistViewModel.setPlaylist(playlist.toPlaylist())
//                                                        playlistViewModel.getPlaylist(playlist.id, TYPE_PLAYLIST)
//                                                    }
//
//
//                                                    ReadyPlaylist(
//                                                        playlist = playlist.toPlaylist(),
//                                                        songs = songs,
//                                                        playerController = playerController
//                                                    ) {
//                                                        if (songs.itemSnapshotList.items.isNotEmpty()) {
//                                                            playerController.init(
//                                                                data.rows.indexOf(
//                                                                    playlist
//                                                                ), songs.itemSnapshotList.items
//                                                            )
//                                                        } else {
//                                                            Log.d(
//                                                                "ReadyPlaylist",
//                                                                "Songs are not yet loaded for playlist ID: ${playlist.id}"
//                                                            )
//                                                        }
//                                                    }
//                                                }
                                            }

                                            data.isMiks() -> {
                                                item {
                                                    // Заменяем MiksView на Image
                                                    // URL фотографии из вашего бэкендa

                                                    val songs = homeViewModel.mixSongs.collectAsLazyPagingItems()

                                                    Image(
                                                        painter = painterResource(id = R.drawable.miks_img),
                                                        contentDescription = "Miks",
                                                        modifier = Modifier
                                                            .width(configuration.screenWidthDp.dp - 30.dp)
                                                            .fillMaxHeight()
                                                            .clickable {
                                                                // Filter out nulls from the paging list
                                                                val songList = songs.itemSnapshotList.items

                                                                if (songList.isNotEmpty()) {
                                                                    playerController.init(0, songList)
                                                                    playerController.originalTracks.addAll(songList)
                                                                }
                                                            }
                                                    )
                                                }
                                            }

                                            data.isNewTMSongs() -> {
                                                items(data.rows.chunked(4)) { group ->
                                                    Column(
                                                        verticalArrangement = Arrangement.spacedBy(
                                                            10.dp
                                                        )

                                                    ) {
                                                        group.forEachIndexed { index, item ->

                                                            val globalIndex =
                                                                data.rows.indexOf(item) + 1

                                                            LastListened(
                                                                song = item.toSong(),
                                                                playerController = playerController,
//                                                                modifier = Modifier.width(150.dp),
                                                                onMoreClick = {
                                                                    selectedSong = item.toSong()
                                                                    showMoreDialog = true
                                                                }) {
                                                                playerController
                                                                    .init(
                                                                        globalIndex - 1,
                                                                        data.toSongList()
                                                                    )
                                                                songsViewModel.listenedSong(item.toSong().id)

                                                                playerController.originalTracks.addAll(data.toSongList())  }
                                                        }

                                                    }
                                                }
                                            }

                                            data.isTopSongs() -> {

                                                if (data.id == "top10songs") {
                                                    items(data.rows.take(10).chunked(4)) { group ->
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            )

                                                        ) {
                                                            group.forEachIndexed { index, item ->

                                                                val globalIndex =
                                                                    data.rows.indexOf(item) + 1

                                                                SongItem(
                                                                    song = item.toSong(),
                                                                    playerController = playerController,
                                                                    order = globalIndex,
//                                                                modifier = Modifier.width(150.dp),
                                                                    onMoreClick = {
                                                                        selectedSong = item.toSong()
                                                                        showMoreDialog = true
                                                                    }) {
                                                                    playerController
                                                                        .init(
                                                                            globalIndex - 1,
                                                                            data.toSongList()
                                                                        )
                                                                    songsViewModel.listenedSong(item.toSong().id)
                                                                    playerController.originalTracks.addAll(data.toSongList())
                                                                }
                                                            }

                                                        }
                                                    }

                                                } else {
                                                    items(data.rows.chunked(4)) { group ->
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            )

                                                        ) {
                                                            group.forEachIndexed { index, item ->

                                                                val globalIndex =
                                                                    data.rows.indexOf(item) + 1

                                                                LastListened(
                                                                    song = item.toSong(),
                                                                    playerController = playerController,
//                                                                modifier = Modifier.width(150.dp),
                                                                    onMoreClick = {
                                                                        selectedSong = item.toSong()
                                                                        showMoreDialog = true
                                                                    }) {
                                                                    playerController
                                                                        .init(
                                                                            globalIndex - 1,
                                                                            data.toSongList()
                                                                        )
                                                                    songsViewModel.listenedSong(item.toSong().id)
                                                                    playerController.originalTracks.addAll(data.toSongList())
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            }


                                            data.isAlbums() -> {
                                                items(data.rows,
                                                    key = { playlist -> playlist.id }) { album ->
                                                    AnimatedVisibility(visible = true) {

                                                        PlaylistView(
                                                            playlist = album.toPlaylist(),
                                                            hasFixedSize = true
                                                        ) {
                                                            navigator.navigate(
                                                                PlaylistScreenDestination(
                                                                    TYPE_ALBUM, album.toPlaylist()
                                                                )
                                                            )
                                                        }
                                                    }

                                                }
                                            }


                                            data.isArtists() -> {
                                                items(data.rows,
                                                    key = { artist -> artist.id }) { artist ->
                                                    ArtistView(
                                                        artist = artist.toArtist(),
                                                        hasFixedSize = true
                                                    ) {
                                                        navigator.navigate(
                                                            ArtistScreenDestination(
                                                                baseRequest = BaseRequest(
                                                                    artistId = artist.id
                                                                )
                                                            )
                                                        )
                                                    }

                                                }

                                            }

                                            data.isPlaylistArray() -> {
                                                items(data.rows,
                                                    key = { playlist -> playlist.id }) { playlist ->
                                                    PlaylistView(
                                                        playlist = playlist.toPlaylist(),
                                                        hasFixedSize = true
                                                    ) {
                                                        navigator.navigate(
                                                            PlaylistScreenDestination(
                                                                TYPE_PLAYLIST, playlist.toPlaylist()
                                                            )
                                                        )
                                                    }
                                                }
                                            }


                                            data.isPlaylist() -> {
                                                itemsIndexed(items = data.rows,
                                                    key = { _, song -> song.id }) { index, item ->
                                                    SongView(song = item.toSong(),
                                                        playerController = homeViewModel.getPlayerController(),
                                                        onMoreClick = {
                                                            selectedSong = item.toSong()
                                                            showMoreDialog = true
                                                        }) {
                                                        homeViewModel.getPlayerController()
                                                            .init(index, data.toSongList())
                                                        homeViewModel.getPlayerController().originalTracks.addAll(data.toSongList())
                                                    }
                                                }
                                            }

                                            data.isTopPlaylist() -> {
                                                itemsIndexed(
                                                    items = data.rows,
                                                    key = { _, item -> item.id }) { index, item ->
                                                    SongView(song = item.toSong(),
                                                        playerController = homeViewModel.getPlayerController(),
                                                        isTop = true,
//                                                        order = index + 1,
                                                        onMoreClick = {
                                                            selectedSong = item.toSong()
                                                            showMoreDialog = true
                                                        }) {
                                                        homeViewModel.getPlayerController()
                                                            .init(index, data.toSongList())
                                                        homeViewModel.listen(item.id)
                                                        homeViewModel.getPlayerController().originalTracks.addAll(data.toSongList())
                                                    }
                                                }
                                            }

                                            data.isMedia() -> {
                                                itemsIndexed(
                                                    items = data.rows,
                                                    key = { _, item -> item.id }) { index, item ->
                                                    MediaView(
                                                        media = item.toMedia(),
                                                        onMoreClick = {},
                                                        onClick = {
                                                            navigator.navigate(
                                                                VideoPlayerScreenDestination(url = item.link)
                                                            )

                                                        }
                                                    )
                                                }
                                            }

                                            data.isShow() -> {
                                                itemsIndexed(
                                                    items = data.rows,
                                                    key = { _, item -> item.id }) { index, item ->
                                                    MediaView(
                                                        media = item.toShow(),
                                                        onMoreClick = {},
                                                        onClick = {
                                                            navigator.navigate(
                                                                MediasScreenDestination(
                                                                    title = item.title,
                                                                    baseRequest = BaseRequest(
                                                                        showId = item.id
                                                                    )
                                                                )
                                                            )
                                                        }
                                                    )
                                                }
                                            }

                                            data.isNews() -> {

                                                itemsIndexed(
                                                    items = data.rows,
                                                    key = { _, item -> item.id }) { index, item ->
                                                    NewsView(
                                                        news = item.toNews(),
                                                        onClick = {
                                                            navigator.navigate(
                                                                NewsDetailScreenDestination(
                                                                    item.id
                                                                )
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
            }
            PullRefreshIndicator(
                refreshing = uiState.data?.isNotEmpty() == true && uiState.isLoading,
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
                            homeViewModel.insertSong(song)
                            homeViewModel.listen(song.id, true)
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

                    }
                )
            }
        }

    }
}

