package tm.bent.dinle.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.R
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.WhiteMilk


@Composable
fun SwitchListItemView(
    icon: Int,
    title: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onToggle(!isChecked)
        }
        .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)) {

        Icon(
            painterResource(id = icon),
            contentDescription = null,
            tint = WhiteMilk

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
                color = MaterialTheme.colorScheme.onBackground,
            ),

            )

        Switch(
            modifier = Modifier.scale(0.8f),
            checked = isChecked,
            onCheckedChange = { isChecked ->
                onToggle(isChecked)
            },
            colors = SwitchDefaults.colors(
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.onBackground,
                checkedThumbColor =  MaterialTheme.colorScheme.onBackground,
                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground,
                uncheckedTrackColor = MaterialTheme.colorScheme.background
            )
        )


    }
}
