package tm.bent.dinle.ui.destinations.playlists

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.PlaylistRowView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.components.search.SearchBar
import tm.bent.dinle.ui.destinations.PlaylistScreenDestination
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel.Companion.TYPE_PLAYLIST
import tm.bent.dinle.ui.destinations.searchresults.SearchViewModel
import tm.bent.dinle.ui.theme.Divider


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PlaylistsScreen(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {

    val playlistsViewModel = hiltViewModel<PlaylistsViewModel>()

    LaunchedEffect(baseRequest) {
        playlistsViewModel.setBaseRequest(baseRequest)
    }


    val playlists = playlistsViewModel.playlists.collectAsLazyPagingItems()

    val isRefreshing = playlists.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current


    val pullRefreshState =
        rememberPullRefreshState(refreshing = playlists.itemCount != 0 && playlists.loadState.refresh == LoadState.Loading,
            onRefresh = {
                playlists.retry()
                playlists.refresh()
            })



    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
            .pullRefresh(pullRefreshState),
        topBar = {
            Column {

                SimpleTopAppBar(title = stringResource(id = R.string.playlists)) {
                    navigator.navigateUp()
                }
                SearchBar() { s ->
                    playlistsViewModel.setBaseRequest(baseRequest.copy( search = s ))
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    playlists.retry()
                    playlists.refresh()

                }
            }
        },
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            if (playlists.itemCount == 0 && playlists.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier)
            } else if (playlists.itemCount == 0 && playlists.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier) {
                    playlists.retry()
                    playlists.refresh()
                }
            } else if (playlists.itemCount == 0) {
                NotFoundView(Modifier)
            } else {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(playlists.itemCount) { index ->
                        val playlist = playlists[index]!!
                        PlaylistRowView(
                            onMoreClick = {},
                            playlist = playlist
                        ) {
                            navigator.navigate(
                                PlaylistScreenDestination(
                                    TYPE_PLAYLIST, playlist
                                )
                            )
                        }
                        HorizontalDivider(
                            color = Divider, modifier = Modifier.padding(start = 90.dp)
                        )
                    }

                }

            }
            PullRefreshIndicator(
                refreshing = playlists.itemCount != 0 && playlists.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }


}


@Destination
@Composable
fun SearchPlaylists(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {


    val playlistsViewModel = hiltViewModel<PlaylistsViewModel>()
    val searchViewModel = hiltViewModel<SearchViewModel>()

    LaunchedEffect(baseRequest) {
        playlistsViewModel.setBaseRequest(baseRequest)
    }



    val playlists = playlistsViewModel.playlists.collectAsLazyPagingItems()

    val isRefreshing = playlists.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current


    val pullRefreshState =
        rememberPullRefreshState(refreshing = playlists.itemCount != 0 && playlists.loadState.refresh == LoadState.Loading,
            onRefresh = {
                playlists.retry()
                playlists.refresh()
            })



    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            if (playlists.itemCount == 0 && playlists.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier)
            } else if (playlists.itemCount == 0 && playlists.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier) {
                    playlists.retry()
                    playlists.refresh()
                }
            } else if (playlists.itemCount == 0) {
                NotFoundView(Modifier)
            } else {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(playlists.itemCount) { index ->
                        val playlist = playlists[index]!!
                        PlaylistRowView(
                            onMoreClick = {},
                            playlist = playlist
                        ) {
                            navigator.navigate(
                                PlaylistScreenDestination(
                                    TYPE_PLAYLIST, playlist
                                )
                            )
                        }
                        HorizontalDivider(
                            color = Divider, modifier = Modifier.padding(start = 90.dp)
                        )
                    }

                }

            }
            PullRefreshIndicator(
                refreshing = playlists.itemCount != 0 && playlists.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }


}