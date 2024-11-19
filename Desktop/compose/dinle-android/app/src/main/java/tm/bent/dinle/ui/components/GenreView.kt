package tm.bent.dinle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import tm.bent.dinle.domain.model.Genre
import tm.bent.dinle.ui.theme.RobotoFlex


@Composable
fun GenreView(
    genre: Genre,
    onClick: () -> Unit
) {

    Box {

        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.small)
                .aspectRatio(1.7f)
                .clickable { onClick() },
            model = genre.getImage(),
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
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            text = genre.name,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 16.8.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(600),
                color = Color.White

                ),
            overflow = TextOverflow.Ellipsis
        )


    }
}

