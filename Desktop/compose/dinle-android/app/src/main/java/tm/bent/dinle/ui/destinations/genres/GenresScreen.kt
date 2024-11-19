package tm.bent.dinle.ui.destinations.genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.ui.components.GenreView
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.pullRefresh
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.GenreScreenDestination
import tm.bent.dinle.ui.destinations.genre.GenreViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun GenresScreen(
    baseRequest: BaseRequest,
    navigator: DestinationsNavigator
) {

    val genreViewModel = hiltViewModel<GenreViewModel>()

    LaunchedEffect(baseRequest){
        genreViewModel.setBaseRequest(baseRequest)
    }

    val uiState by genreViewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading && uiState.data != null,
        onRefresh = {
            genreViewModel.getGenres(baseRequest)
        }
    )

    Scaffold(
        modifier = Modifier
            .pullRefresh(pullRefreshState),
        topBar = {
            Column {
                SimpleTopAppBar(title = stringResource(id = R.string.genres)) {
                    navigator.navigateUp()
                }
            }}
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (uiState.isLoading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (uiState.isFailure) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    genreViewModel.getGenres(baseRequest)
                }
            } else if (uiState.data?.size == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    uiState.data?.let { data ->
                        items(data) { genre ->

                            GenreView(
                                genre = genre,
                                onClick = {
                                    navigator.navigate(GenreScreenDestination(genre))
                                }
                            )


                        }

                    }


                }

            }
            PullRefreshIndicator(
                refreshing = uiState.isLoading && uiState.data != null,
                pullRefreshState,
            )
        }

    }

}
