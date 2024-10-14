package tm.bent.dinle.ui.destinations.downloads

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import tm.bent.dinle.R
import tm.bent.dinle.di.BASE_URL
import tm.bent.dinle.di.SHARE_SONG_URL
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.ui.components.DeletingDialog
import tm.bent.dinle.ui.components.DownloadSongRowView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.SongRowView
import tm.bent.dinle.ui.components.bottomsheet.SongActionsBottomSheet
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.util.ShareUtils


@OptIn(ExperimentalMaterial3Api::class)
@Destination()
@Composable
fun DownloadsScreen(
    navigator: DestinationsNavigator
) {


    val songViewModel = hiltViewModel<DownloadsViewModel>()

    val context = LocalContext.current

    val songs by songViewModel.getDownloadedSongs().collectAsState(initial = emptyList())


    val playerController = songViewModel.getPlayerController()
    val downloadTracker = songViewModel.getDownloadTracker()



    val lazyListState = rememberLazyListState()


    val moreDialogState = rememberModalBottomSheetState()
    var showMoreDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    val (isDeletingVisible, setDeletingVisible) = remember { mutableStateOf(false) }




    Scaffold(
        topBar = {
            SimpleTopAppBar(stringResource(id = R.string.downloads)) {
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

                    items(songs) { song ->

                        DownloadSongRowView(
                            song = song,
                            playerController = playerController,
                            onMoreClick = {
                                selectedSong = song
                                showMoreDialog = true
                            },
                            isLikeable = false,
                            onLike = {
                                songViewModel.likeSong(song.id)
                            },
                            onDownload = {
                                songViewModel.insertSong(song)
                                songViewModel.listen(song.id, true)
                            },
                            downloadTracker = downloadTracker,
                            onClick = {
                                selectedSong = song
                                playerController.init(songs.indexOf(song),songs)
                            },
                            deletingVisible = {
                                selectedSong = song
                                setDeletingVisible(true)
                            }
                        )
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
            if (isDeletingVisible) {
                DeletingDialog(title = "Pozmak isleyanizmi",
                    onConfirm = {
                        val indx = songs.indexOf(selectedSong!!)
                        Log.e("myLog", "SongRowView: $indx")
                        songViewModel.removeSongAt(indx)
                        setDeletingVisible(false)
                        selectedSong!!.isPlaying() == false
                    },
                    onDismissRequest = {
                        setDeletingVisible(false)
                    })
            }
        }

    }


}
