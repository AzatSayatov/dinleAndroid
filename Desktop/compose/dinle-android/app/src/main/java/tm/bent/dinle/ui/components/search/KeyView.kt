package tm.bent.dinle.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.theme.RobotoFlex


@Composable
fun KeyView(
    key: String,
    onSearch: (String) -> Unit,
) {

    Row( modifier = Modifier
        .fillMaxWidth()
        .clickable { onSearch(key) }
        .padding(20.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = key,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 16.8.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Icon(
            tint = Color.Unspecified,
            painter = painterResource(id = R.drawable.ic_north_east),
            contentDescription = null
        )
    }
}