package tm.bent.dinle.ui.destinations.artistinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.ui.components.LibraryItemView
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.toolbar.CollapsingToolbarScaffold
import tm.bent.dinle.ui.components.toolbar.ScrollStrategy
import tm.bent.dinle.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import tm.bent.dinle.ui.destinations.GenresScreenDestination
import tm.bent.dinle.ui.theme.Black80
import tm.bent.dinle.ui.theme.Container
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.util.formatDateWithDots


@Destination
@Composable
fun ArtistInfoScreen(
    navigator: DestinationsNavigator,
    id: String,
) {
    val artistInfoViewModel = hiltViewModel<ArtistInfoViewModel>()

    val uiState by artistInfoViewModel.uiState.collectAsState()


    LaunchedEffect(id) {
        artistInfoViewModel.getArtistInfo(id)
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
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .blur(radius = 100.dp)
                            .aspectRatio(0.9f)
                            .graphicsLayer {
                                alpha = state.toolbarState.progress
                            }
                            .background(Inactive),
                        model = uiState.data?.getImage(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Black80)
                            .blur(radius = 10.dp)
                    )


                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp, bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .width(235.dp)
                                .aspectRatio(1f)
                                .graphicsLayer {
                                    alpha = state.toolbarState.progress
                                }
                                .clip(CircleShape)
                                .background(Inactive),
                            model = uiState.data?.getImage(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,

                            )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 15.dp),
                            text = uiState.data?.title ?: "",
                            style = TextStyle(
                                color = Color.White, fontSize = 22.sp, fontWeight = FontWeight(600)
                            ),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = stringResource(id = R.string.artist),
                            style = TextStyle(
                                color = Inactive, fontSize = 14.sp, fontWeight = FontWeight(400)
                            ),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                    IconButton(modifier = Modifier.padding(vertical = 10.dp), onClick = {
                        navigator.navigateUp()
                    }) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null
                        )
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

                }


            }) {

            Box(
                contentAlignment = Alignment.TopCenter
            ) {
                if (uiState.isLoading) {
                    LoadingView(Modifier.fillMaxWidth())
                } else if (uiState.isFailure) {
                    NoConnectionView(Modifier) {
                        artistInfoViewModel.getArtistInfo(id)
                    }
                } else {
                    uiState.data?.let { artistInfo ->
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 20.dp),
                                text = artistInfo.about.toString(),
                                style = TextStyle(
                                    color = Inactive2,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(400)
                                ),
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 20.dp, vertical = 20.dp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = stringResource(id = R.string.about_artist),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = RobotoFlex,
                                    fontWeight = FontWeight.Bold,
                                )
                            )


                            Column(

                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(Container)
                            ) {

                                LibraryItemView(
                                    icon = R.drawable.ic_music_note,
                                    title = stringResource(id = R.string.songs),
                                    subtitle = artistInfo.count.songs.toString(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_album,
                                    title = stringResource(id = R.string.albums),
                                    subtitle = artistInfo.count.alboms.toString(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_music_note_circle,
                                    title = stringResource(id = R.string.genres),
                                    subtitle = artistInfo.getGenres(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                ) {
                                    navigator.navigate(GenresScreenDestination(BaseRequest(artistId = id)))
                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_calendar,
                                    title = stringResource(id = R.string.date),
                                    subtitle = artistInfo.createdAt.formatDateWithDots(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }
                            }

                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 20.dp, vertical = 20.dp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = stringResource(id = R.string.statistics),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = RobotoFlex,
                                    fontWeight = FontWeight.Bold,
                                )
                            )


                            Column(

                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(Container)
                            ) {

                                LibraryItemView(
                                    icon = R.drawable.ic_listener,
                                    title = stringResource(id = R.string.listeners),
                                    subtitle = artistInfo.count.songListeners.toString(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_follower,
                                    title = stringResource(id = R.string.followers),
                                    subtitle = artistInfo.count.followers.toString(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_music_note_heart,
                                    title = stringResource(id = R.string.likes),
                                    subtitle = artistInfo.count.songLikers.toString(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_download_circle,
                                    title = stringResource(id = R.string.downloads),
                                    subtitle = artistInfo.count.downloads.toString(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }
                            }
                        }
                    }

                }

            }
        }
    }
}
