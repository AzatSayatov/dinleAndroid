package tm.bent.dinle.ui.destinations.albums

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
import tm.bent.dinle.ui.destinations.playlistdetail.PlaylistViewModel.Companion.TYPE_ALBUM
import tm.bent.dinle.ui.destinations.searchresults.SearchViewModel
import tm.bent.dinle.ui.theme.Divider


@OptIn(ExperimentalMaterial3Api::class)
@Destination()
@Composable
fun AlbumsScreen(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {



    val albumViewModel = hiltViewModel<AlbumViewModel>()

    LaunchedEffect(baseRequest){
        albumViewModel.setBaseRequest(baseRequest)
    }


    val albums = albumViewModel.albums.collectAsLazyPagingItems()

    val isRefreshing = albums.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = albums.itemCount != 0 &&  albums.loadState.refresh == LoadState.Loading,
        onRefresh = {
            albums.retry()
            albums.refresh()
        }
    )
    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
        topBar = {
            Column {
                SimpleTopAppBar(title = stringResource(id = R.string.albums)) {
                    navigator.navigateUp()
                }

                SearchBar() { s ->
                    baseRequest.apply { search = s }
                    albumViewModel.setBaseRequest(baseRequest)
                    albums.retry()
                    albums.refresh()

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
            if (albums.itemCount == 0 && albums.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (albums.itemCount == 0 && albums.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    albums.retry()
                    albums.refresh()
                }
            }else if (albums.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(albums.itemCount) {index ->
                        val album = albums[index]!!
                        PlaylistRowView(
                            playlist = album,
                            onMoreClick = {}
                        ) {
                            navigator.navigate(
                                PlaylistScreenDestination(
                                    TYPE_ALBUM,
                                    album
                                )
                            )
                        }
                        HorizontalDivider(
                            color = Divider,
                            modifier = Modifier
                                .padding(start = 90.dp)
                        )
                    }

                }

            }
            PullRefreshIndicator(
                refreshing =albums.itemCount != 0 &&  albums.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }



}

@Destination()
@Composable
fun SearchAlbums(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {

    val searchViewModel = hiltViewModel<SearchViewModel>()


    val albumViewModel = hiltViewModel<AlbumViewModel>()

    LaunchedEffect(baseRequest){
        albumViewModel.setBaseRequest(baseRequest)
    }


    val albums = albumViewModel.albums.collectAsLazyPagingItems()

    val isRefreshing = albums.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = albums.itemCount != 0 &&  albums.loadState.refresh == LoadState.Loading,
        onRefresh = {
            albums.retry()
            albums.refresh()
        }
    )
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
            if (albums.itemCount == 0 && albums.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (albums.itemCount == 0 && albums.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    albums.retry()
                    albums.refresh()
                }
            }else if (albums.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(albums.itemCount) {index ->
                        val album = albums[index]!!
                        PlaylistRowView(
                            playlist = album,
                            onMoreClick = {}
                        ) {
                            navigator.navigate(
                                PlaylistScreenDestination(
                                    TYPE_ALBUM,
                                    album
                                )
                            )
                        }
                        HorizontalDivider(
                            color = Divider,
                            modifier = Modifier
                                .padding(start = 90.dp)
                        )
                    }

                }

            }
            PullRefreshIndicator(
                refreshing =albums.itemCount != 0 &&  albums.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }



}