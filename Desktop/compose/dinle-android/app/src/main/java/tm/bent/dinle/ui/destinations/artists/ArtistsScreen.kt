package tm.bent.dinle.ui.destinations.artists

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import tm.bent.dinle.R
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.ui.components.ArtistRowView
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.components.search.SearchBar
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.theme.Divider


@Destination
@Composable
fun ArtistsScreen(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {

    val artistsViewModel = hiltViewModel<ArtistsViewModel>()

    LaunchedEffect(baseRequest) {
        Log.e("ARTISTS", "ArtistsScreen: $baseRequest")
        artistsViewModel.setBaseRequest(baseRequest)
    }


    val artists = artistsViewModel.artists.collectAsLazyPagingItems()

    val isRefreshing = artists.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = artists.itemCount != 0 && artists.loadState.refresh == LoadState.Loading,
        onRefresh = {
            artists.retry()
            artists.refresh()
        }
    )
    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
        topBar = {
            Column {
                SimpleTopAppBar(title = stringResource(id = R.string.artists)) {
                    navigator.navigateUp()
                }
                SearchBar() { s ->
                    artistsViewModel.setBaseRequest(baseRequest.apply { search = s })
                    artists.retry()
                    artists.refresh()

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
            if (artists.itemCount == 0 && artists.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (artists.itemCount == 0 && artists.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    artists.retry()
                    artists.refresh()
                }
            } else if (artists.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(artists.itemCount) { index ->
                        val artist = artists[index]!!

                        ArtistRowView(
                            artist = artist,
                            onClick = {
                                navigator.navigate(ArtistScreenDestination(BaseRequest(artistId = artist.id )))
                            }
                        )
                        HorizontalDivider(
                            color = Divider,
                            modifier = Modifier
                                .padding(start = 90.dp)
                        )
                    }

                }
            }
            PullRefreshIndicator(
                refreshing = artists.itemCount != 0 && artists.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }


}


@Destination
@Composable
fun SearchArtists(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {

    val artistsViewModel = hiltViewModel<ArtistsViewModel>()

    LaunchedEffect(baseRequest) {
        artistsViewModel.setBaseRequest(baseRequest)
    }


    val artists = artistsViewModel.artists.collectAsLazyPagingItems()

    val isRefreshing = artists.loadState.refresh is LoadState.Loading

    val lazyListState = rememberLazyListState()
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            lazyListState.scrollToItem(0)
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = artists.itemCount != 0 && artists.loadState.refresh == LoadState.Loading,
        onRefresh = {
            artists.retry()
            artists.refresh()
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
            if (artists.itemCount == 0 && artists.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (artists.itemCount == 0 && artists.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    artists.retry()
                    artists.refresh()
                }
            } else if (artists.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    items(artists.itemCount) { index ->
                        val artist = artists[index]!!

                        ArtistRowView(
                            artist = artist,
                            onClick = {
                                navigator.navigate(ArtistScreenDestination(BaseRequest(artistId = artist.id )))
                            }
                        )
                        HorizontalDivider(
                            color = Divider,
                            modifier = Modifier
                                .padding(start = 90.dp)
                        )
                    }

                }
            }
            PullRefreshIndicator(
                refreshing = artists.itemCount != 0 && artists.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }


}