package tm.bent.dinle.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.domain.model.Artist
import tm.bent.dinle.ui.DinleImage
import tm.bent.dinle.ui.theme.RobotoFlex


@Composable
fun ArtistView(
    artist: Artist,
    hasFixedSize:Boolean = false,
    onClick: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val width = if(hasFixedSize)140.dp else (configuration.screenWidthDp.dp - 70.dp)/2

    Column(modifier = Modifier
        .width(IntrinsicSize.Min)
        .clip(shape = MaterialTheme.shapes.extraSmall)
        .clickable { onClick() }
        .padding(4.dp)) {
        
        DinleImage(modifier = Modifier
            .size(width)
            .heightIn(0.dp, 118.dp)
            .widthIn(0.dp, 118.dp)
            .clip(shape = CircleShape)
            .aspectRatio(1f) ,
            model = artist.getImage())

//        SubcomposeAsyncImage(
//            modifier = Modifier
//                .size(width)
//                .heightIn(0.dp, 200.dp)
//                .widthIn(0.dp, 200.dp)
//                .clip(shape = CircleShape)
//                .aspectRatio(1f),
//            model = artist.getImage(),
//            contentScale = ContentScale.Crop,
//            contentDescription = "",
//            alignment = Alignment.Center,
//            loading = {
//                GreyShimmer { brush ->
//                    Spacer(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(brush)
//                    )
//                }
//            },
//            error = {
//                GreyShimmer { brush ->
//                    Spacer(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(brush)
//                    )
//                }
//            },
//
//        )



        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), text = artist.title, style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 16.8.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(700),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center

                ), maxLines = 1, overflow = TextOverflow.Ellipsis
        )


    }

}

