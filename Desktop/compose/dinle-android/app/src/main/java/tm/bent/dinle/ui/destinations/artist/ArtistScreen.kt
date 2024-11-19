package tm.bent.dinle.ui.destinations.artist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.launch
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.di.SHARE_ARTIST_URL
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.ui.components.CustomIconButton
import tm.bent.dinle.ui.components.DeletingDialog
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.MediaRowView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SongRowView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.components.toolbar.CollapsingToolbarScaffold
import tm.bent.dinle.ui.components.toolbar.ScrollStrategy
import tm.bent.dinle.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import tm.bent.dinle.ui.destinations.ArtistInfoScreenDestination
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.VideoPlayerScreenDestination
import tm.bent.dinle.ui.destinations.albums.SearchAlbums
import tm.bent.dinle.ui.destinations.downloads.DownloadsViewModel
import tm.bent.dinle.ui.destinations.songs.SongsViewModel
import tm.bent.dinle.ui.theme.BackgroundGray
import tm.bent.dinle.ui.theme.Black80
import tm.bent.dinle.ui.theme.ButtonBackground
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.util.ShareUtils


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ArtistScreen(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {
    val artistViewModel = hiltViewModel<ArtistViewModel>()
    val songViewModel = hiltViewModel<SongsViewModel>()


    val uiState by artistViewModel.uiState.collectAsState()


    val downloadsViewModel = hiltViewModel<DownloadsViewModel>()
    val songs by downloadsViewModel.getDownloadedSongs().collectAsState(initial = emptyList())
    val (isDeletingVisible, setDeletingVisible) = remember { mutableStateOf(false) }


    val context = LocalContext.current
    var following  by rememberSaveable {
        mutableStateOf(
            uiState.data?.artist?.following
        )
    }


    LaunchedEffect(uiState.data?.artist){
        following = uiState.data?.artist?.following
    }

    LaunchedEffect(baseRequest) {
        artistViewModel.getArtistDetail(baseRequest)
    }




    var followingIcon = if (following == true) R.drawable.ic_check else R.drawable.ic_add
    var followingStr = if (following == true) R.string.unfollow else R.string.follow


    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }


    val state = rememberCollapsingToolbarScaffoldState()

    val playerController = artistViewModel.getPlayerController()

    val downloadTracker = artistViewModel.getDownloadTracker()
    val scrollState = rememberScrollState()


    val tabs = listOf(stringResource(id = R.string.songs),
        stringResource(id = R.string.media),
        stringResource(id = R.string.albums))

    var selectedTab by rememberSaveable { mutableStateOf(0) }

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold { padding ->
        CollapsingToolbarScaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallax(1f)
                        .aspectRatio(0.8f)
                        .road(Alignment.Center, Alignment.TopCenter)
                        .background(color = MaterialTheme.colorScheme.background)
                        .alpha(state.toolbarState.progress),

                    contentAlignment = Alignment.Center
                ) {
                    Box {

                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .blur(radius = 100.dp)
                                .aspectRatio(0.8f)
                                .graphicsLayer {
                                    alpha = 0.9f
                                }
                                .background(Inactive),
                            model = uiState.data?.artist?.getImage(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.8f)) // Настройте степень прозрачности
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
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp, bottom = 20.dp),
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
                                .clip(CircleShape)
                                .background(Inactive),
                            model = uiState.data?.artist?.getImage(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,

                            )

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 15.dp),
                            text = uiState.data?.artist?.title ?: "",
                            style = TextStyle(
                                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(600)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = stringResource(id = R.string.artist),
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
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = ButtonBackground,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    uiState.data?.artist?.let { artist ->
                                        following = !following!!
                                        artistViewModel.subscribe(artist.id)
                                        Log.i("ArtistId", "ArtistScreen: ${artist.id}")
                                    }
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.White,
                                    painter = painterResource(id = followingIcon),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier.padding(start = 10.dp),
                                    text = stringResource(id = followingStr),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(400)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                            }

                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 10.dp)
                                    .background(BackgroundGray),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ButtonBackground,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    uiState.data?.songs?.let { songs ->
                                        if (songs.isNotEmpty()) playerController.init(0, songs)
                                    }
                                },

                                ) {

                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White,
                                    painter = painterResource(id = R.drawable.ic_play),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier.padding(start = 10.dp),
                                    text = stringResource(id = R.string.play),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(400)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )


                            }
                        }
                    }


                }






                Row(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding(), start = 10.dp)
                        .fillMaxWidth()
                        .pin(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomIconButton(
                        modifier = Modifier
                            .padding(vertical = 10.dp),
                        icon = R.drawable.ic_back
                    ) {
                        navigator.navigateUp()
                    }

                    if (state.toolbarState.progress == 0f) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f),
                            text = uiState.data?.artist?.title ?: "",
                            style = TextStyle(
                                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(600)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (state.toolbarState.progress == 0f) {
                        CustomIconButton(
                            modifier = Modifier.padding(end = 20.dp),
                            icon = R.drawable.ic_play,
                            color = MaterialTheme.colorScheme.onBackground
                        ) {
                            uiState.data?.songs?.let { songs ->
                                if (songs.isNotEmpty()) playerController.init(0, songs)
                            }
                        }
                    } else {

                        Row {
                            CustomIconButton(
                                modifier = Modifier.padding(end = 10.dp), icon = R.drawable.ic_info
                            ) {
                                uiState.data?.artist?.id?.let {
                                    navigator.navigate(ArtistInfoScreenDestination(it))
                                }
                            }
                            CustomIconButton(
                                modifier = Modifier.padding(end = 20.dp), icon = R.drawable.ic_share
                            ) {
                                ShareUtils.shareLink(
                                    context = context,
                                    link = "$SHARE_ARTIST_URL${baseRequest.artistId}"
                                )
                            }
                        }

                    }

                }


            }) {


            Column (
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            when {
                                dragAmount < -70f && selectedTab < tabs.size - 1 -> {
                                    selectedTab ++
                                    coroutineScope.launch{
                                        lazyListState.animateScrollToItem(selectedTab)
                                    }
                                }

                                dragAmount > 70f && selectedTab > 0 -> {
                                    selectedTab --
                                    coroutineScope.launch{
                                        lazyListState.animateScrollToItem(selectedTab)
                                    }
                                }

                            }
                        }
                    }
            ){
                Box(
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (uiState.isLoading) {
                        LoadingView(Modifier.fillMaxWidth())
                    } else if (uiState.isFailure) {
                        NoConnectionView(Modifier) {
                            artistViewModel.getArtistDetail(body = baseRequest)
                        }
                    } else {

                        Column {
                            TabRow(
                                selectedTabIndex = selectedTab,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color.Black),
                                containerColor = Color.Black,
                                contentColor = Color.White,
                                indicator = { tabPositions ->
                                    TabRowDefaults.Indicator(
                                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                        color = Color.White // Устанавливаем цвет индикатора на белый
                                    )
                                }
                            ) {
                                tabs.forEachIndexed { index, title ->
                                    Tab(selected = selectedTab == index,
                                        onClick = { selectedTab = index },
                                        text = { Text(text = title)},
                                        )
                                }
                            }
                            when(selectedTab){
                                0 -> {
                                    if (!uiState.data?.songs.isNullOrEmpty()) {
//                                        SearchSong(baseRequest = baseRequest, navigator = navigator)

                                        val songs = songViewModel.songs.collectAsLazyPagingItems()

                                        Box(
//                                            modifier = Modifier.padding(top = padding.calculateTopPadding()),
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

                                                    items(songs.itemCount) { index ->
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
//                                                                playerController.init(
//                                                                    song,
//                                                                    songs.itemSnapshotList.items
//                                                                )
                                                                playerController.init(songs.itemSnapshotList.items.indexOf(song),
                                                                    songs.itemSnapshotList.items)
                                                            },
                                                            isDeletingVisible = {
                                                                selectedSong = song
                                                                setDeletingVisible(true)
                                                            }
                                                        )
                                                    }

                                                }
                                            }
                                        }

                                    }else {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("No clips available", color = Color.Black)
                                        }
                                    }

                                }

                                2 -> {
                                    if (!uiState.data?.alboms.isNullOrEmpty()) {
                                        SearchAlbums(navigator = navigator,
                                            baseRequest = baseRequest)
                                    }else {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("No clips available", color = Color.Black)
                                        }
                                    }

                                }
                                1 -> {
                                    if (!uiState.data?.clips.isNullOrEmpty()) {
                                        LazyColumn(
                                            contentPadding = PaddingValues(vertical = 10.dp),
                                        ) {
                                            items(uiState.data?.clips!!) { clip ->
                                                MediaRowView(media = clip) {
                                                    navigator.navigate(
                                                        VideoPlayerScreenDestination(url = clip.link)
                                                    )
                                                }
                                            }
                                        }
                                    }else {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("No clips available", color = Color.Black)
                                        }
                                    }

                                }
                            }
                        }
                    }
//                        LazyColumn(
//                            contentPadding = PaddingValues(vertical = 10.dp)
//                        ) {
//
//                            if (!uiState.data?.songs.isNullOrEmpty()) {
//                                item {
//                                    Column {
//                                        HeaderView(stringResource(id = R.string.songs)) {
//                                            navigator.navigate(
//                                                SongsScreenDestination(
//                                                    baseRequest = baseRequest
//                                                )
//                                            )
//                                        }
//                                        Spacer(modifier = Modifier.height(10.dp))
//                                        uiState.data?.songs?.forEach { song ->
//                                            SongRowView(
//                                                song = song,
//                                                playerController = playerController,
//                                                downloadTracker = downloadTracker,
//                                                onMoreClick = {
//                                                    selectedSong = song
//                                                    showMoreDialog = true
//                                                },
//                                                onDownload = {
//                                                    artistViewModel.insertSong(song)
//                                                    artistViewModel.listen(song.id, true)
//
//                                                },
//                                                onClick = {
//                                                    uiState.data?.songs?.let {
//                                                        playerController.init(
//                                                            song, it
//                                                        )
//                                                    }
//                                                },
//                                            )
//                                        }
//                                        Spacer(modifier = Modifier.height(10.dp))
//
//                                    }
//                                }
//                            }
//                            if (!uiState.data?.clips.isNullOrEmpty()) {
//                                item {
//                                    Column {
//
//                                        HeaderView(stringResource(id = R.string.media)) {
//                                            navigator.navigate(
//                                                MediasScreenDestination(
//                                                    baseRequest = baseRequest
//                                                )
//                                            )
//                                        }
//
//                                        LazyRow(
//                                            contentPadding = PaddingValues(20.dp, 10.dp),
//                                            horizontalArrangement = Arrangement.spacedBy(20.dp)
//                                        ) {
//                                            items(
//                                                uiState.data?.clips!!,
//                                                key = { media -> media.id }) { media ->
//                                                MediaView(
//                                                    media = media,
//                                                    onMoreClick = {},
//                                                    onClick = {
//                                                        navigator.navigate(
//                                                            VideoPlayerScreenDestination(url = media.link)
//                                                        )
//
//                                                    }
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            if (!uiState.data?.alboms.isNullOrEmpty()) {
//                                item {
//
//                                    Column {
//                                        HeaderView(stringResource(id = R.string.albums)) {
//                                            navigator.navigate(
//                                                PlaylistsScreenDestination(
//                                                    baseRequest = baseRequest
//                                                )
//                                            )
//                                        }
//
//                                        LazyRow(
//                                            contentPadding = PaddingValues(20.dp, 10.dp),
//                                            horizontalArrangement = Arrangement.spacedBy(20.dp)
//                                        ) {
//                                            items(
//                                                uiState.data?.alboms!!,
//                                                key = { playlist -> playlist.id }) { playlist ->
//                                                PlaylistView(
//                                                    playlist = playlist, hasFixedSize = true
//                                                ) {
//                                                    navigator.navigate(
//                                                        PlaylistScreenDestination(
//                                                            PlaylistViewModel.TYPE_ALBUM, playlist
//                                                        )
//                                                    )
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
                    }

                }



            if (isDeletingVisible) {
                DeletingDialog(title = stringResource(R.string.pozmak_isleyanizmi),
                    onConfirm = {
                        val indx = songs.indexOf(selectedSong!!)
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
                SongActionsBottomSheet(
                    song = selectedSong!!,
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
                            artistViewModel.listen(song.id, true)
                            artistViewModel.insertSong(song)
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