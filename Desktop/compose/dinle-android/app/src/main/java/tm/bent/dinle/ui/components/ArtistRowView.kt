package tm.bent.dinle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import tm.bent.dinle.domain.model.Artist
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.RobotoFlex


@Composable
fun ArtistRowView(
    artist: Artist,
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
                .size(60.dp)
                .clip(shape = CircleShape),
            model = artist.getImage(),
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



        Text(
            modifier = Modifier.weight(1f),
            text = artist.title, style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 19.2.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start

            ), maxLines = 1, overflow = TextOverflow.Ellipsis
        )


    }

}
