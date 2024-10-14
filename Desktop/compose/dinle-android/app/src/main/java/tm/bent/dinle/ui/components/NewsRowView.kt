package tm.bent.dinle.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.domain.model.News
import tm.bent.dinle.ui.DinleImage
import tm.bent.dinle.ui.theme.RobotoFlex

@Composable
fun NewsRowView(
    news: News,
    onClick: () -> Unit,
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
                model = news.cover,
            )

        }


        Text(
            modifier = Modifier.fillMaxWidth(), text = news.title, style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 19.2.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground
            ), maxLines = 3, overflow = TextOverflow.Ellipsis
        )

    }
}


@Preview(showBackground = true)
@Composable
fun asfdd() {
    NewsRowView(news = News(
        "DSFSDfsd",
        "DSFSDfsd",
        "DSFSDfsd",
        "DSFSDfsd",
        "DSFSDfsd",
        0
    )) {

    }
}