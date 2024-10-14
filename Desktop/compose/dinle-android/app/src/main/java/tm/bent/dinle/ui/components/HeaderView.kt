package tm.bent.dinle.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.R
import tm.bent.dinle.ui.destinations.ProfileScreenDestination
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.util.clickWithoutIndication

@Composable
fun HeaderView(
    title: String,
    extendable: Boolean = true,
    onClick: () -> Unit
){
    val navigator: DestinationsNavigator

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickWithoutIndication { onClick() }
            .padding(horizontal = 20.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(
                    weight = 1.0f,
                    fill = false,
                ),
            softWrap = false,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontFamily = RobotoFlex,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground,
            )
        )

        if (extendable) {
            Icon(
                painterResource(id = R.drawable.ic_arr_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground

            )
        }
    }
}
