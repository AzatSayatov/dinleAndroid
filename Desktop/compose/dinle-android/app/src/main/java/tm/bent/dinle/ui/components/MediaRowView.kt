package tm.bent.dinle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.R
import tm.bent.dinle.domain.model.Media
import tm.bent.dinle.domain.model.mockMedia
import tm.bent.dinle.ui.DinleImage
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.util.formatDuration
import tm.bent.dinle.ui.util.formatTime


@Composable
fun MediaRowView(
    media: Media,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(20.dp, 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Box {

            DinleImage(
                modifier = Modifier
                    .height(70.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .aspectRatio(1.7f),
                model = media.cover,
            )

            if (media.duration != 0L) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clip(shape = MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        .padding(4.dp, 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_play_arrow),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground

                    )

                    Text(
                        text = media.duration.formatDuration(),
                        style = TextStyle(
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(500),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(), text = media.title, style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.2.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = MaterialTheme.colorScheme.onBackground
                ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun ClipsRowView(
    media: Media,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(20.dp, 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Box {

            DinleImage(
                modifier = Modifier
                    .height(70.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .aspectRatio(1.7f),
                model = media.cover,
            )

            if (media.duration != 0L) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clip(shape = MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        .padding(4.dp, 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_play_arrow),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground

                    )

                    Text(
                        text = media.duration.formatDuration(),
                        style = TextStyle(
                            fontSize = 10.sp,
                            lineHeight = 12.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(500),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(), text = media.title, style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.2.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = MaterialTheme.colorScheme.onBackground
                ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun asf() {
    MediaRowView(media = mockMedia) {

    }
}