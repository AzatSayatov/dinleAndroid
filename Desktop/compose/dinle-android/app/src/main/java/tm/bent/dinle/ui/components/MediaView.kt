package tm.bent.dinle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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


@Composable
fun MediaView(
    media: Media,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
) {


    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .clickable { onClick() }
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Box {

            DinleImage(
                modifier = Modifier
                    .height(105.dp)
                    .width(135.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .aspectRatio(1.7f)
                   ,
                model = media.cover,
            )

            if (media.duration != 0L){
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .padding(bottom = 5.dp)
                        .clip(shape = MaterialTheme.shapes.extraSmall)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                        .padding(4.dp, 2.dp)
                    ,
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = media.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = MaterialTheme.colorScheme.onBackground,

                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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


@Preview(showBackground = true)
@Composable
fun asawdf() {
    MediaView(media = mockMedia, onClick = { }) {

    }
}