package tm.bent.dinle.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.Playlist
import tm.bent.dinle.domain.model.mockPlaylist
import tm.bent.dinle.ui.DinleImage
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Subtitle


@Composable
fun ReadyPlaylist(
    playlist: Playlist = mockPlaylist,
    onClick: () -> Unit
) {

    val configuration = LocalConfiguration.current
//    val width = if(hasFixedSize)140.dp else (configuration.screenWidthDp.dp - 70.dp)/2

    Box(
        modifier = Modifier
            .width(155.dp)
            .height(50.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
                Log.i("ReadyPlaylist", "ReadyPlaylist: " + playlist.id)
            }
    ) {


        // Фотография как фон
        Box(
            modifier = Modifier
                .width(155.dp)
                .height(90.dp)
                .clip(shape = RoundedCornerShape(8.dp))

        ) {

            // Фотография как фон
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(80.dp)
                    .widthIn(0.dp, 150.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .aspectRatio(1f)
            ) {
                DinleImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(20.dp),
                    model = playlist.getImage(),
                )

                // Полупрозрачный черный фон
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.65f)) // Настройте степень прозрачности
                )
            }

            // Текст поверх изображения
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart) // Располагаем текст внизу
                    .padding(start = 5.dp, bottom = 2.dp, end = 5.dp)
                    .widthIn(0.dp,125.dp), // Отступы от краев
                text = playlist.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(700),
                    color = Color.White, // Цвет текста, чтобы был виден на фоне изображения
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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



    @Composable
    fun tView(
        playlist: Playlist,
        hasFixedSize: Boolean = false,
        onClick: () -> Unit,
    ) {

        val configuration = LocalConfiguration.current
        val width = if (hasFixedSize) 140.dp else (configuration.screenWidthDp.dp - 70.dp) / 2

        Column(modifier = Modifier
            .width(IntrinsicSize.Min)
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .clickable { onClick() }
            .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {

            DinleImage(
                modifier = Modifier
                    .size(115.dp)
                    .heightIn(0.dp, 200.dp)
                    .widthIn(0.dp, 200.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .aspectRatio(1f),
                model = playlist.getImage()
            )



            Text(
                modifier = Modifier.fillMaxWidth(), text = playlist.title, style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 16.8.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.onBackground,

                    ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )

            if (playlist.description?.isNotEmpty() == true) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = playlist.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.8.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(400),
                        color = Subtitle,

                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }


        }
    }
}



