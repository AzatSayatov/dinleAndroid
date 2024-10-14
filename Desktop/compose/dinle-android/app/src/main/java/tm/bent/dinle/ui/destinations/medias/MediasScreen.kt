package tm.bent.dinle.ui.destinations.medias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.MediaRowView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.VideoPlayerScreenDestination
import tm.bent.dinle.ui.destinations.medias.MediaViewModel
import tm.bent.dinle.ui.destinations.searchresults.SearchViewModel

@Destination
@Composable
fun MediasScreen(
    title:String? = null,
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {

    val mediaViewModel = hiltViewModel<MediaViewModel>()

    LaunchedEffect(baseRequest) {
        mediaViewModel.setBaseRequest(baseRequest)
    }


    val medias = mediaViewModel.medias.collectAsLazyPagingItems()


    val pullRefreshState =
        rememberPullRefreshState(refreshing = medias.itemCount != 0 && medias.loadState.refresh == LoadState.Loading,
            onRefresh = {
                medias.retry()
                medias.refresh()
            })
    Scaffold(
        modifier = Modifier.pullRefresh(pullRefreshState),
        topBar = {
            Column {
                SimpleTopAppBar(title = title ?: stringResource(id = R.string.shows)) {
                    navigator.navigateUp()
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
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(medias.itemCount) { index ->
                        val show = medias[index]!!
                        MediaRowView(
                            media = show,
                            onClick = {
                                navigator.navigate(VideoPlayerScreenDestination(url = show.link))
                            }
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = medias.itemCount != 0 && medias.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }
}

@Destination
@Composable
fun SearchMedia(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
    onClick : () -> Unit
) {

    val mediaViewModel = hiltViewModel<MediaViewModel>()

    val searchViewModel = hiltViewModel<SearchViewModel>()

    LaunchedEffect(baseRequest) {
        mediaViewModel.setBaseRequest(baseRequest)
    }


    val medias = mediaViewModel.medias.collectAsLazyPagingItems()


    val pullRefreshState =
        rememberPullRefreshState(refreshing = medias.itemCount != 0 && medias.loadState.refresh == LoadState.Loading,
            onRefresh = {
                medias.retry()
                medias.refresh()
            })
    Scaffold(
        modifier = Modifier.pullRefresh(pullRefreshState),
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
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
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(medias.itemCount) { index ->
                        val show = medias[index]!!
                        MediaRowView(
                            media = show,
                            onClick = {
//                                navigator.navigate(VideoPlayerScreenDestination(url = show.link))
                                onClick()
                            }
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = medias.itemCount != 0 && medias.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }
}