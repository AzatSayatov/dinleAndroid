package tm.bent.dinle.ui.destinations.newsdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.R
import tm.bent.dinle.di.SHARE_NEWS_URL
import tm.bent.dinle.ui.components.CustomIconButton
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.toolbar.CollapsingToolbarScaffold
import tm.bent.dinle.ui.components.toolbar.ScrollStrategy
import tm.bent.dinle.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import tm.bent.dinle.ui.theme.Black80
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.util.ShareUtils
import tm.bent.dinle.ui.util.formatDateWithDots


@Destination
@Composable
fun NewsDetailScreen(
    id: String,
    navigator: DestinationsNavigator,
) {
    val newsDetailViewModel = hiltViewModel<NewsDetailViewModel>()

    val uiState by newsDetailViewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(id) {
        newsDetailViewModel.getNewsDetail(id)
    }

    val state = rememberCollapsingToolbarScaffoldState()

    Scaffold { padding ->
        CollapsingToolbarScaffold(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallax(1f)
                        .background(color = MaterialTheme.colorScheme.background)
                        .alpha(state.toolbarState.progress),

                    contentAlignment = Alignment.Center
                ) {

                    Box{


                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .blur(radius = 100.dp)
                                .aspectRatio(0.9f)
                                .graphicsLayer {
                                    alpha = 0.9f
                                }
                                .background(Inactive),
                            model = uiState.data?.cover,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.8f)) // Настройте степень прозрачности
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Black80)
                            .blur(radius = 10.dp)
                    )


                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp, bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .height(173.dp)
                                .aspectRatio(1.7f)
                                .graphicsLayer {
                                    alpha = state.toolbarState.progress
                                }
                                .clip(MaterialTheme.shapes.medium)
                                .background(Inactive),
                            model = uiState.data?.cover,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,

                            )


                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .fillMaxWidth()
                        .pin(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    CustomIconButton(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .padding(vertical = 10.dp),
                        icon = R.drawable.ic_back
                    ) {
                        navigator.navigateUp()
                    }

                    if (state.toolbarState.progress == 0f) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .weight(1f),
                            text = uiState.data?.title ?: "",
                            style = TextStyle(
                                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(600)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    CustomIconButton(
                        modifier = Modifier.padding(end = 20.dp), icon = R.drawable.ic_share
                    ) {
                        ShareUtils.shareLink(context = context, link = SHARE_NEWS_URL)
                    }

                }


            }) {

            Box(
                contentAlignment = Alignment.TopCenter
            ) {
                if (uiState.isLoading) {
                    LoadingView(Modifier.fillMaxWidth())
                } else if (uiState.isFailure) {
                    NoConnectionView(Modifier) {
                        newsDetailViewModel.getNewsDetail(id)
                    }
                } else {
                    uiState.data?.let { newsDetail ->
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {


                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 15.dp),
                                text = stringResource(id = R.string.news),
                                style = TextStyle(
                                    color = Inactive, fontSize = 14.sp, fontWeight = FontWeight(400)
                                ),
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row {

                                Text(
                                    modifier = Modifier
                                        .width(300.dp)
                                        .padding(horizontal = 20.dp),
                                    text = newsDetail.title,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight(700)
                                    ),
                                    textAlign = TextAlign.Start,
                                )

                                Box {
                                    Row {

                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_eye),
                                            contentDescription = null
                                        )


                                        Text(modifier = Modifier
                                            .padding(top = 2.dp, start = 2.dp),

                                            text = newsDetail.viewed.toString(),
                                            style = TextStyle(
                                                color = Inactive,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(400)
                                            ),
                                            textAlign = TextAlign.Start,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }


                                }
                            }

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .padding(top = 15.dp),
                                text = newsDetail.createdAt.formatDateWithDots(),
                                style = TextStyle(
                                    color = Inactive,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(400)
                                ),
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 20.dp, vertical = 20.dp
                                    ),
                                text = uiState.data?.description ?: "",
                                style = TextStyle(
                                    color = Color(0xFF656565),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(500)
                                ),
                                textAlign = TextAlign.Start,
                            )


                        }
                    }

                }

            }
        }
    }
}
