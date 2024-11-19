package tm.bent.dinle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import tm.bent.dinle.domain.model.Playlist
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex


@Composable
fun PlaylistRowView(
    playlist: Playlist,
    onMoreClick: () -> Unit,
    onClick: () -> Unit,
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(20.dp, 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        SubcomposeAsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall),
            model = playlist.getImage(),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            alignment = Alignment.Center,
                    loading = {
                GreyShimmer { brush ->
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush)
                    )
                }
            },
            error = {
                GreyShimmer { brush ->
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush)
                    )
                }
            },
        )



        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = playlist.title, style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start

                ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                text = playlist.description ?: "", style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 16.8.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = Inactive2,
                    textAlign = TextAlign.Start

                ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }

//        IconButton(onClick = onMoreClick) {
//            Icon(
//                modifier = Modifier.size(26.dp),
//                painter = painterResource(id = R.drawable.ic_more_hr),
//                contentDescription = null,
//                tint = Color.White
//            )
//        }


    }
}