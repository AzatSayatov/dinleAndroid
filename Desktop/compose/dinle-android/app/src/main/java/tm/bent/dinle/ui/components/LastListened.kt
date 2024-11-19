package tm.bent.dinle.ui.components


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.Song
import tm.bent.dinle.player.PlayerController
import tm.bent.dinle.ui.DinleSongRowImage
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Subtitle


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LastListened(
    song: Song,
    playerController: PlayerController,
    onMoreClick: () -> Unit,
    onClick: () -> Unit,
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.equalizer)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    Column(
        modifier = Modifier
//            .fillMaxWidth()
            .width(300.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 3.dp, end = 5.dp, bottom = 5.dp, top = 5.dp)
            .combinedClickable(onClick = { onClick() },
                onLongClick = { onMoreClick()},
                onDoubleClick = {onClick()})
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            DinleSongRowImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(Inactive2),
                model = song.getImage()
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (playerController.selectedTrack?.id == song.id) {
                        LottieAnimation(
                            composition, progress, modifier = Modifier
                                .padding(end = 5.dp)
                                .size(24.dp)
                        )
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(), text = song.title, style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 19.2.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(600),
                            color = MaterialTheme.colorScheme.onBackground,

                            ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth(), text = song.description, style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 19.2.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(400),
                        color = Subtitle,

                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                )

            }

            IconButton(
                onClick = { onMoreClick() }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter =  painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.onBackground
                )

            }

        }

    }
}
