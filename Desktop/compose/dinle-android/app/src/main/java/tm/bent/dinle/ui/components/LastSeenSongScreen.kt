package tm.bent.dinle.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.downloads.DownloadsViewModel
import tm.bent.dinle.ui.destinations.home.HomeViewModel
import tm.bent.dinle.ui.destinations.songs.SongsViewModel
import tm.bent.dinle.ui.util.ShareUtils


@OptIn(ExperimentalMaterial3Api::class)
@Destination()
@Composable
fun LastSeenSongs(
    navigator: DestinationsNavigator
) {


    val songsViewModel = hiltViewModel<SongsViewModel>()


    val playerController = songsViewModel.getPlayerController()

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val downloadTracker = homeViewModel.getDownloadTracker()


    val downloadsViewModel = hiltViewModel<DownloadsViewModel>()
    val songs by downloadsViewModel.getDownloadedSongs().collectAsState(initial = emptyList())
    val (isDeletingVisible, setDeletingVisible) = remember { mutableStateOf(false) }



    val lazyListState = rememberLazyListState()


    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }

    LaunchedEffect(Unit) {
        songsViewModel.getLastSeenSongs()
    }

    // Observe the state of last seen songs
    val lastSeenSongs by songsViewModel.lastSeenSongs.collectAsState()



    Scaffold(
        topBar = {
            SimpleTopAppBar(stringResource(id = R.string.last_listened)) {
                navigator.navigateUp()
            }
        },
    ) { padding ->

        Box(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            contentAlignment = Alignment.TopCenter
        ) {

            LazyColumn(
                state = lazyListState
            ) {

                if (lastSeenSongs != null && lastSeenSongs!!.isNotEmpty()) {
                    items(
                        items = lastSeenSongs!!,
                        key = { song -> song.id } // Используем уникальный ключ для каждого элемента
                    ) { song ->
                        SongRowView(
                            song = song,
                            playerController = playerController,
                            onMoreClick = {
                                selectedSong = song
                                showMoreDialog = true
                            },
                            isLikeable = false,
                            onLike = {
                                songsViewModel.likeSong(song.id)
                            },
                            onDownload = {
                                songsViewModel.insertSong(song)
                                songsViewModel.listen(song.id, true)
                            },
                            downloadTracker = downloadTracker,
                            onClick = {
                                playerController.init(lastSeenSongs!!.indexOf(song),
                                    lastSeenSongs!!
                                )
                                playerController.onTrackClick(song)
                            },
                            isDeletingVisible = {
                                selectedSong = song
                                setDeletingVisible(true)
                            }
                        )
                    }

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
                            songsViewModel.insertSong(song)
                            songsViewModel.listen(song.id, true)
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






