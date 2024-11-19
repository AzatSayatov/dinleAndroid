package tm.bent.dinle.ui.destinations.songinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.ui.components.ArtistView
import tm.bent.dinle.ui.components.LibraryItemView
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.toolbar.CollapsingToolbarScaffold
import tm.bent.dinle.ui.components.toolbar.ScrollStrategy
import tm.bent.dinle.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.GenresScreenDestination
import tm.bent.dinle.ui.theme.Black80
import tm.bent.dinle.ui.theme.Container
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.util.formatDateWithDots


@Destination
@Composable
fun SongInfoScreen(
    id: String,
    navigator: DestinationsNavigator,
) {
    val songInfoViewModel = hiltViewModel<SongInfoViewModel>()

    val uiState by songInfoViewModel.uiState.collectAsState()


    LaunchedEffect(id) {
        songInfoViewModel.getSongInfo(id)
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
                        model = uiState.data?.cover,
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
                                .clip(MaterialTheme.shapes.medium)
                                .background(Inactive),
                            model = uiState.data?.cover,
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
                            text = uiState.data?.artist?.title ?: "",
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
                        songInfoViewModel.getSongInfo(id)
                    }
                } else {
                    uiState.data?.let { songInfo ->
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 20.dp, end = 20.dp, top = 20.dp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = stringResource(id = R.string.about_song),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = RobotoFlex,
                                    fontWeight = FontWeight.Bold,
                                )
                            )


                            if (songInfo.about.isNotEmpty()) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp, end = 20.dp, top = 10.dp
                                        ),
                                    text = songInfo.about,
                                    style = TextStyle(
                                        color = Inactive2,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(400)
                                    ),
                                    textAlign = TextAlign.Start,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }


                            Column(

                                modifier = Modifier
                                    .padding(
                                        start = 20.dp, end = 20.dp, top = 10.dp,
                                    )
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(Container)
                            ) {


                                LibraryItemView(
                                    icon = R.drawable.ic_album,
                                    title = stringResource(id = R.string.albums),
                                    subtitle = songInfo.count.alboms.toString(),
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
                                    subtitle = songInfo.getGenres(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                ) {
                                    navigator.navigate(GenresScreenDestination(BaseRequest(songId = id)))
                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

//                                LibraryItemView(
//                                    icon = R.drawable.ic_music_note,
//                                    title = stringResource(id = R.string.duration),
//                                    subtitle = "00:00",
//                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
//                                    extendable = false
//                                ) {
//
//                                }
//
//                                HorizontalDivider(
//                                    color = MaterialTheme.colorScheme.background
//                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_calendar,
                                    title = stringResource(id = R.string.date),
                                    subtitle = songInfo.createdAt.formatDateWithDots(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }
                            }


                            if (uiState.data?.duets.isNullOrEmpty()){
                                val duets = uiState.data?.duets!!
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp, vertical = 20.dp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    text = stringResource(id = R.string.roles),
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        lineHeight = 24.sp,
                                        fontFamily = RobotoFlex,
                                        fontWeight = FontWeight.Bold,
                                    )
                                )
                                LazyRow( Modifier
                                    .fillMaxWidth(),
                                    contentPadding = PaddingValues(20.dp, 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(duets,
                                        key = { duet -> duet.artist.id }) { duet ->
                                        ArtistView(
                                            artist = duet.artist,
                                            hasFixedSize = true
                                        ) {
                                            navigator.navigate(
                                                ArtistScreenDestination(
                                                    baseRequest = BaseRequest(
                                                        artistId = duet.artist.id
                                                    )
                                                )
                                            )
                                        }

                                    }
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
                                    subtitle = songInfo.count.songListeners.toString(),
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
                                    subtitle = songInfo.count.followers.toString(),
                                    subtitleColor = MaterialTheme.colorScheme.onBackground,
                                    extendable = false
                                ) {

                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.background
                                )

                                LibraryItemView(
                                    icon = R.drawable.ic_music_note_heart,
                                    title = stringResource(id = R.string.favorite_songs),
                                    subtitle = songInfo.count.songLikers.toString(),
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
                                    subtitle = songInfo.count.downloads.toString(),
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
