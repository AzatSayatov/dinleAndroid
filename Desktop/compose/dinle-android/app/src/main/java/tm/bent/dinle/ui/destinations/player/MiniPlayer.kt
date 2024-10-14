package tm.bent.dinle.ui.destinations.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import tm.bent.dinle.R
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.PlaybackState
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniPlayer(
    song: Song,
    playerController: PlayerController,
    onClick: () -> Unit,
) {


    val playbackStateValue = playerController.playbackState.collectAsState(
        initial = PlaybackState(0L, 0L)
    ).value

    val currentMediaProgress = playbackStateValue.currentPlaybackPosition.toFloat()
    val currentPosTemp by rememberSaveable { mutableStateOf(0f) }

    val pagerState =
        androidx.compose.foundation.pager.rememberPagerState(
            playerController.selectedTrackIndex,
            pageCount = { playerController.tracks.size })

    LaunchedEffect(playerController.selectedTrackIndex) {

        if (playerController.selectedTrackIndex >= 0 && pagerState.currentPage != playerController.selectedTrackIndex) {
            delay(300)
            pagerState.scrollToPage(playerController.selectedTrackIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage) {

        if (playerController.selectedTrackIndex >= 0 && playerController.selectedTrackIndex < playerController.tracks.size && pagerState.currentPage != playerController.selectedTrackIndex) {
            delay(400)
            playerController.onTrackClick(playerController.tracks[pagerState.currentPage])
        }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 10.dp)
    ) {

//        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
//            Slider(value = if (currentPosTemp == 0f) currentMediaProgress else currentPosTemp,
//                onValueChange = {},
//                onValueChangeFinished = {},
//                enabled = false,
//                valueRange = 0f..playbackStateValue.currentTrackDuration.toFloat(),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 5.dp),
//                colors = SliderDefaults.colors(
//                    activeTrackColor = MaterialTheme.colorScheme.primary,
//                    inactiveTrackColor = Inactive2,
//                ),
//                thumb = {
//                    SliderDefaults.Thumb(
//                        modifier = Modifier.offset(x = 0.dp),
//                        interactionSource = remember { MutableInteractionSource() },
//                        thumbSize = DpSize(0.dp, 0.dp)
//                    )
//                },
//                track = {
//                    SliderDefaults.Track(modifier = Modifier
//                        .height(3.dp)
//                        .padding(0.dp)
//                        .defaultMinSize(1.dp, 1.dp),
//                        sliderState = it,
//                        thumbTrackGapSize = 0.dp,
//                        drawStopIndicator = {})
//                }
//
//            )
//        }

        Row(

            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f),
//                contentPadding = PaddingValues(horizontal = 10.dp)
            ) { page ->
                val son = playerController.tracks[page]
                val songImagePainter = rememberAsyncImagePainter(
                    model = son.getImage(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(shape = MaterialTheme.shapes.small)
                            .background(Inactive2),
                        painter = songImagePainter,
                        contentDescription = "artist image",
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            modifier = Modifier.basicMarquee(
                                iterations = Int.MAX_VALUE,
                            ),
                            text = son.title,
                            fontFamily = RobotoFlex,
                            fontSize = 16.sp,
                            lineHeight = 19.2.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = son.description,
                            fontFamily = RobotoFlex,
                            fontSize = 14.sp,
                            lineHeight = 16.8.sp,
                            fontWeight = FontWeight.Normal,
                            color = Inactive2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }



            IconButton(
                onClick = { playerController.onPlayPauseClick() },
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter = if (song.isPlaying()) painterResource(id = R.drawable.ic_pause) else painterResource(
                        id = R.drawable.ic_play
                    ),
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.onBackground
                )

            }

            IconButton(
                onClick = { playerController.onNextClick() }
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter =  painterResource(id = R.drawable.ic_skip_forward),
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.onBackground
                )

            }


        }


    }
}


