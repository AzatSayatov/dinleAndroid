package tm.bent.dinle.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.components.GreyShimmer


@Composable
fun DinleImage(
    modifier: Modifier, model: String
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = model,
        contentScale = ContentScale.Crop,
        contentDescription = "",
        alignment = Alignment.Center,
        loading = {

            Box(
                contentAlignment = Alignment.Center
            ) {
                GreyShimmer { brush ->
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_placeholder),
                    contentDescription = null
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
}


@Composable
fun DinleSongImage(
    modifier: Modifier, model: String
) {
    Box {

        DinleImage(
            modifier = modifier.aspectRatio(1f),
            model = model,
        )

//        Icon(
//            modifier = Modifier
//                .padding(5.dp)
//                .align(Alignment.TopEnd),
//            painter = painterResource(id = R.drawable.ic_logo_mini),
//            contentDescription = "song setting",
//            tint = Color.White
//        )

        Icon(
            modifier = Modifier
                .padding(5.dp)
                .background(
                    shape = CircleShape, color = MaterialTheme.colorScheme.background.copy(
                        alpha = 0.3f
                    )
                )
                .clip(CircleShape)
                .padding(1.dp)
                .align(Alignment.BottomEnd),
            painter = painterResource(id = R.drawable.ic_play_circle),
            contentDescription = "song setting",
            tint = Color.White
        )


    }
}

@Composable
fun DinleSongRowImage(
    modifier: Modifier,
    model: String
){
    SubcomposeAsyncImage(
        modifier = modifier,
        model = model,
        contentScale = ContentScale.Crop,
        contentDescription = "",
        alignment = Alignment.Center,
        loading = {

            Box(
                contentAlignment = Alignment.Center
            ) {
                GreyShimmer { brush ->
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush)
                    )
                }
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
}

@Composable
fun DinlePlaylistImage(
    modifier: Modifier, model: String
) {
    Box {

        DinleImage(
            modifier = modifier.aspectRatio(1f),
            model = model,
        )

        Icon(
            modifier = Modifier
                .padding(5.dp)
                .background(
                    shape = CircleShape, color = MaterialTheme.colorScheme.background.copy(
                        alpha = 0.3f
                    )
                )
                .clip(CircleShape)
                .padding(1.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(id = R.drawable.ic_play_circle),
            contentDescription = "song setting",
            tint = Color.White
        )


    }
}