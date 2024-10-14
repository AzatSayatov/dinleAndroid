package tm.bent.dinle.ui.destinations.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.R
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.ui.components.LastListened
import tm.bent.dinle.ui.components.LastSeenSongs
import tm.bent.dinle.ui.components.LibraryItemView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.AlbumsScreenDestination
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.ArtistsScreenDestination
import tm.bent.dinle.ui.destinations.DownloadsScreenDestination
import tm.bent.dinle.ui.destinations.LastSeenSongsDestination
import tm.bent.dinle.ui.destinations.PlaylistScreenDestination
import tm.bent.dinle.ui.destinations.PlaylistsScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.SongsScreenDestination
import tm.bent.dinle.ui.destinations.home.HomeViewModel
import tm.bent.dinle.ui.destinations.player.SongItem
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel
import tm.bent.dinle.ui.destinations.songs.SongsViewModel
import tm.bent.dinle.ui.theme.Container
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.util.ShareUtils
import tm.bent.dinle.ui.util.clickWithoutIndication


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun LibraryScreen(
    navigator: DestinationsNavigator
) {
    val songsViewModel = hiltViewModel<SongsViewModel>()
    val playerController = songsViewModel.getPlayerController()

    val context = LocalContext.current

    val brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xff090909), Color(0xff111111)
        )
    )

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val playlistViewModel = hiltViewModel<PlaylistViewModel>()


//    val playerController = homeViewModel.getPlayerController()
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

    // Fetch the last seen songs
    LaunchedEffect(Unit) {
        songsViewModel.getLastSeenSongs()
    }

    // Observe the state of last seen songs
    val lastSeenSongs by songsViewModel.lastSeenSongs.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.media_library),
                        style = TextStyle(
                            fontSize = 22.sp,
                            lineHeight = 26.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(700),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    )
                }, colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .padding(16.dp, 10.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Container)
                .verticalScroll(rememberScrollState())
        ) {

            Box(modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(Color(0XFF131313))) {

                Column(
                    modifier = Modifier
                        .padding(16.dp, 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {


                    LibraryItemView(
                        icon = R.drawable.ic_arr_download,
                        title = stringResource(id = R.string.downloads),
                    ) {
                        navigator.navigate(DownloadsScreenDestination)
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background
                    )

                    Box{

                        LibraryItemView(
                            icon = R.drawable.ic_licked_songs,
                            title = stringResource(id = R.string.favorite_songs),
                        ) {
                            if (uiState.isFailure){

                            }else {
                                val baseRequest = BaseRequest(isLiked = true)

                                navigator.navigate(
                                    SongsScreenDestination(
                                        baseRequest = baseRequest
                                    )
                                )
                            }
                        }

                        if (uiState.isFailure){
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.3f)) // Настройте степень прозрачности
                            )
                        }
                    }


                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background
                    )

                    Box{
                        LibraryItemView(
                            icon = R.drawable.ic_add_queue,
                            title = stringResource(id = R.string.playlists),
                        ) {
                            if (uiState.isFailure){

                            }else {
                                val baseRequest = BaseRequest(isLiked = true)

                                navigator.navigate(
                                    PlaylistsScreenDestination(
                                        baseRequest = baseRequest
                                    )
                                )
                            }
                        }

                        if (uiState.isFailure){
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.3f)) // Настройте степень прозрачности
                            )
                        }
                    }


                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background
                    )

                    Box {
                        LibraryItemView(
                            icon = R.drawable.ic_follower,
                            title = stringResource(id = R.string.following_artists),
                        ) {
                            if (uiState.isFailure) {

                            } else {
                                val baseRequest = BaseRequest(isLiked = true)
                                navigator.navigate(
                                    ArtistsScreenDestination(
                                        baseRequest = baseRequest
                                    )
                                )
                            }
                        }

                        if (uiState.isFailure) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.3f)) // Настройте степень прозрачности
                            )
                        }

                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background
                    )

                    Box {
                        LibraryItemView(
                            icon = R.drawable.ic_album,
                            title = stringResource(id = R.string.albums),
                        ) {
                            if (uiState.isFailure) {

                            } else {
                                val baseRequest = BaseRequest(isLiked = true)
                                navigator.navigate(
                                    AlbumsScreenDestination(
                                        baseRequest = baseRequest
                                    )
                                )
                            }

                        }

                        if (uiState.isFailure){
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.3f)) // Настройте степень прозрачности
                            )
                        }
                    }


                }

            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.background
            )

            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                        .clickWithoutIndication { navigator.navigate(LastSeenSongsDestination) }
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .weight(
                                weight = 1.0f,
                                fill = false,
                            ),
                        softWrap = false,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = stringResource(R.string.last_listened),
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(600),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    )
                    Icon(
                        painterResource(id = R.drawable.ic_arr_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground

                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.background
            )

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)
                    .background(color = MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(10.dp, 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                lastSeenSongs?.let { songs ->
                    // Разбиваем песни на группы по 4 элемента
                    items(songs.chunked(4)) { group ->
                        // Отображаем каждую группу вертикально
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            group.forEach { song ->
                                LastListened(
                                    song = song,
                                    playerController = playerController,
                                    onClick = {
                                       playerController.init(songs.indexOf(song),songs)
                                        // Обработка клика на песню
                                    },
                                    onMoreClick = {
                                        showMoreDialog = true
                                        selectedSong = song
                                        // Обработка клика на кнопку "еще"
                                    }
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