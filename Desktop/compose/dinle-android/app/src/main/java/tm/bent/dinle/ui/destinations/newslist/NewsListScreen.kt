package tm.bent.dinle.ui.destinations.newslist

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
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NewsRowView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.NewsDetailScreenDestination


@Destination
@Composable
fun NewsListScreen(
    baseRequest: BaseRequest = BaseRequest(),
    navigator: DestinationsNavigator,
) {

    val newsListViewModel = hiltViewModel<NewsListViewModel>()

    LaunchedEffect(baseRequest) {
        newsListViewModel.setBaseRequest(baseRequest)
    }


    val newsList = newsListViewModel.newsList.collectAsLazyPagingItems()


    val pullRefreshState =
        rememberPullRefreshState(refreshing = newsList.itemCount != 0 && newsList.loadState.refresh == LoadState.Loading,
            onRefresh = {
                newsList.retry()
                newsList.refresh()
            })
    Scaffold(
        modifier = Modifier.pullRefresh(pullRefreshState),
        topBar = {
            Column {
                SimpleTopAppBar(title = stringResource(R.string.news_t)) {
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
            if (newsList.itemCount == 0 && newsList.loadState.refresh == LoadState.Loading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (newsList.itemCount == 0 && newsList.loadState.refresh is LoadState.Error) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    newsList.retry()
                    newsList.refresh()
                }
            } else if (newsList.itemCount == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(newsList.itemCount) { index ->
                        val news = newsList[index]!!
                        NewsRowView(
                            news = news,
                            onClick = {
                                navigator.navigate(
                                    NewsDetailScreenDestination(
                                        news.id
                                    )
                                )
                            }
                        )
                    }

                }

            }
            PullRefreshIndicator(
                refreshing = newsList.itemCount != 0 && newsList.loadState.refresh == LoadState.Loading,
                pullRefreshState,
            )
        }

    }
}