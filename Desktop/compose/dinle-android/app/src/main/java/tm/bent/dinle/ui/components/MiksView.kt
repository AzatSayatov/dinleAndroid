package tm.bent.dinle.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import tm.bent.dinle.ui.DinleImage
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Subtitle


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MiksView(
    song: Song,
    isTop: Boolean = false,
    playerController: PlayerController,
    order: Int = 1,
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

    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(shape = MaterialTheme.shapes.extraSmall)
        .combinedClickable (onClick = { onClick() },
            onLongClick = {onMoreClick()}
        )
        .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {

        DinleImage(
            modifier = Modifier
                .size(140.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall),
            model = song.getImage()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

//            if (isTop) {
//                Text(
//                    modifier = Modifier.padding(end = 4.dp),
//                    text = order.toString(),
//                    style = TextStyle(
//                        fontSize = 28.sp,
//                        lineHeight = 33.6.sp,
//                        fontFamily = RobotoFlex,
//                        fontWeight = FontWeight.SemiBold,
//                        color = MaterialTheme.colorScheme.onBackground,
//
//                        ),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
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
                                .padding(end = 2.dp)
                                .size(16.dp)
                        )
                    }

                    Text(
                        modifier = Modifier.weight(1f), text = song.title, style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.8.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(700),
                            color = MaterialTheme.colorScheme.onBackground
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }


                Text(
                    modifier = Modifier.fillMaxWidth(), text = song.description, style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.8.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(400),
                        color = Subtitle,

                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }

//            Icon(modifier = Modifier
//                .background(
//                    shape = CircleShape, color = MaterialTheme.colorScheme.onBackground.copy(
//                        alpha = 0.05f
//                    )
//                )
//                .clip(CircleShape)
//                .clickable { onMoreClick() }
//                .padding(4.dp),
//                painter = painterResource(id = R.drawable.ic_more_hr),
//                contentDescription = "song setting",
//                tint = MaterialTheme.colorScheme.onBackground)

        }
    }

}

