package tm.bent.dinle.ui.destinations.shows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.MediaRowView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.MediasScreenDestination
import tm.bent.dinle.ui.theme.Divider

@Destination
@Composable
fun ShowsScreen(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {

    val showsViewModel = hiltViewModel<ShowsViewModel>()

    LaunchedEffect(baseRequest) {
        showsViewModel.setBaseRequest(baseRequest)
    }


    val shows = showsViewModel.shows.collectAsLazyPagingItems()


    val pullRefreshState =
        rememberPullRefreshState(refreshing = shows.itemCount != 0 && shows.loadState.refresh == LoadState.Loading,
            onRefresh = {
                shows.retry()
                shows.refresh()
            })
    Scaffold(
        modifier = Modifier.pullRefresh(pullRefreshState),
        topBar = {
            Column {
                SimpleTopAppBar(title = stringResource(id = R.string.shows)) {
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
            if (shows.itemCount == 0 && shows.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (shows.itemCount == 0 && shows.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    shows.retry()
                    shows.refresh()
                }
            } else if (shows.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(shows.itemCount) { index ->
                        val show = shows[index]!!
                        MediaRowView(
                            media = show,
                            onClick = {
                                navigator.navigate(
                                    MediasScreenDestination(
                                        title = show.title,
                                        baseRequest = BaseRequest(
                                            showId = show.id
                                        )
                                    )
                                )
                            }
                        )
                    }

                }

            }
            PullRefreshIndicator(
                refreshing = shows.itemCount != 0 && shows.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }
}