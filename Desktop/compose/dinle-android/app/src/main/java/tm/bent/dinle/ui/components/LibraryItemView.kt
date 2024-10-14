package tm.bent.dinle.ui.components

import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.R
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex


@Composable
fun LibraryItemView(
    icon: Int,
    title: String,
    subtitle: String? = null,
    color: Color = MaterialTheme.colorScheme.onBackground,
    subtitleColor: Color = Inactive2,
    extendable:Boolean = true,
    onClick: () -> Unit
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)) {

        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = color

        )


        Text(
            modifier = Modifier.weight(weight = 1.0f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = title,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(400),
                color = color,
            ),

        )

        subtitle?.let {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = subtitle,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(400),
                    color = subtitleColor,
                )
            )
        }

        if (extendable){
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_arr_right),
                contentDescription = null,
                tint = color

            )
        }
    }
}
