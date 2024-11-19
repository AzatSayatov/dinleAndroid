package tm.bent.dinle.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import tm.bent.dinle.domain.model.News
import tm.bent.dinle.ui.DinleImage
import tm.bent.dinle.ui.destinations.newsdetail.NewsDetailViewModel
import tm.bent.dinle.ui.theme.RobotoFlex
@Composable
fun NewsView(
    news: News,
    onClick: () -> Unit,
) {
    val newsDetailViewModel = hiltViewModel<NewsDetailViewModel>()
    val uiState by newsDetailViewModel.uiState.collectAsState()
    LaunchedEffect(news.id) {
        newsDetailViewModel.getNewsDetail(news.id)
    }

    Row(
        modifier = Modifier
            .width(260.dp)
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .clickable { onClick() }
            .padding(4.dp),
    ) {
        DinleImage(
            modifier = Modifier
                .height(80.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .aspectRatio(1.6f),
            model = news.cover,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp), // Добавим отступ для разделения текста и изображения
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = news.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth() // Занимаем всю ширину контейнера
            )

            Spacer(modifier = Modifier.height(4.dp)) // Добавляем отступ между заголовком и описанием

            Text(
                text = uiState.data?.description?: "",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Normal,
                    color = Color(0Xff656565),
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth() // Занимаем всю ширину контейнера
            )
        }
    }
}



