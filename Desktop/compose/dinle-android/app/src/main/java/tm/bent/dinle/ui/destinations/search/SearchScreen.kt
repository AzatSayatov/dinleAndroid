package tm.bent.dinle.ui.destinations.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.Search
import tm.bent.dinle.ui.components.GenreView
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.NotFoundView
import tm.bent.dinle.ui.components.pager.HorizontalPagerView
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.components.search.KeyView
import tm.bent.dinle.ui.components.search.SearchBar
import tm.bent.dinle.ui.destinations.GenreScreenDestination
import tm.bent.dinle.ui.destinations.SearchResultsScreenDestination
import tm.bent.dinle.ui.destinations.ShazamScreenDestination
import tm.bent.dinle.ui.destinations.genre.GenreViewModel
import tm.bent.dinle.ui.theme.Border
import tm.bent.dinle.ui.theme.Button1
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.WhiteMilk
import tm.bent.dinle.ui.util.clickWithoutIndication


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
) {

    val genreViewModel = hiltViewModel<GenreViewModel>()

    val uiState by genreViewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isLoading, onRefresh = {
        genreViewModel.getGenres()
        genreViewModel.loadBanners()
    })

    val keys = genreViewModel.keys.collectAsState(initial = Search.getDefaultInstance())
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var focused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        genreViewModel.loadBanners()
    }
    val banners by genreViewModel.banners


    Scaffold(modifier = Modifier, topBar = {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.search), style = TextStyle(
                            fontSize = 22.sp,
                            lineHeight = 26.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    )
                }, colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(24.dp)
                            .clickWithoutIndication { navigator.navigate(ShazamScreenDestination) },
                        painter = painterResource(id = R.drawable.ic_mic),
                        contentDescription = null,
                        tint = WhiteMilk
                    )
                }
            )

            SearchBar(
                focusRequester = focusRequester,
                onFocusChanged = { focus ->
                    focused = focus
                }
            ) { search ->
                genreViewModel.saveSearchStr(search.trim())
                navigator.navigate(SearchResultsScreenDestination(search = search.trim()))
            }

            HorizontalDivider(
                color = Border,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        }

    }) { padding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (uiState.isLoading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (uiState.isFailure) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    genreViewModel.getGenres()
                }
            } else if (uiState.data?.size == 0) {
                NotFoundView(Modifier.fillMaxSize())
            } else {

                Column {

                    if (!banners.isEmpty()) {
                        val pagerState =
                            rememberPagerState(0, pageCount = { banners.size })
//                        var pageKey by remember { mutableStateOf(0) }
//                        val songResponce: SongResponce
//
//                        LaunchedEffect(pageKey) {
//                            delay(3000)
//                            if (pagerState.pageCount <= 1) return@LaunchedEffect
//                            var newPosition = pagerState.currentPage + 1
//                            if (newPosition == banners.size) newPosition = 0
//                            pagerState.animateScrollToPage(newPosition)
//                            pageKey = newPosition
//                        }
                        HorizontalPagerView(
                            banners = banners,
                            pagerState = pagerState,
                            imageHeight = 200,
                            onClick = { /* Обработка клика */ }
                        )
                    }

                    LazyVerticalGrid(
                        modifier = Modifier.padding(top = padding.calculateTopPadding()),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(20.dp)
                    ) {


                        uiState.data?.let { data ->

                            item(
                                span = { GridItemSpan(2) }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.genres),
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        lineHeight = 24.sp,
                                        fontFamily = RobotoFlex,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            items(data) { genre ->

                                GenreView(
                                    genre = genre,
                                    onClick = {
                                        navigator.navigate(
                                            GenreScreenDestination(genre)
                                        )
                                    }
                                )

                            }

                        }
                    }


                }


            }

        }

        if (focused && keys.value.messageList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding())
                    .background(Color.Black)
            ) {
                // Показ элементов истории поиска
                keys.value.messageList.forEach { key ->
                    KeyView(
                        key,
                        onSearch = { search ->
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            navigator.navigate(SearchResultsScreenDestination(search = search))
                        }
                    )
                }

                // Пробел для отступа перед кнопкой
                Spacer(modifier = Modifier.height(10.dp))

                // Кнопка для очистки истории, центрированная по горизонтали
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(5.dp),
                    shape = RoundedCornerShape(16.dp), // Установите радиус закругления
                    color = Button1,
                    onClick = {
                        genreViewModel.clearSearchStr()
                        focused = false
                    }
                ) {
                    Row {
                        Text(
                            text = "Clear History",
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterVertically)
                        )

                        IconButton(onClick = {

                        }) {

                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.ic_del_his),
                                contentDescription = null
                            )
                        }
                    }

                }
            }
        }



        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            pullRefreshState,
        )
    }
}
