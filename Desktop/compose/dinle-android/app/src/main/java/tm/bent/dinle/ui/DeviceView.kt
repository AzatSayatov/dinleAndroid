package tm.bent.dinle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.R
import tm.bent.dinle.domain.model.Device
import tm.bent.dinle.ui.theme.Container
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex

@Composable
fun DeviceView(
    device: Device,
    onDelete : () -> Unit,
    isThisDevice: Boolean = true
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { }
            .background(Container)
            .padding(12.dp)
            ,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_android),
            contentDescription = null
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = device.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(500),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            )

            Row {

                Text(
                    text = device.os,
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 17.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Normal,
                        color = Inactive2,
                    )
                )

                Text(text = "|",
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 17.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Normal,
                        color = Inactive2,
                    ),
                    modifier = Modifier.padding(horizontal = 2.dp))


                Text(
                    text = device.version,
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 17.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Normal,
                        color = Inactive2,
                    )
                )

                Text(text = "|",
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 17.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Normal,
                        color = Inactive2,
                    ),
                    modifier = Modifier.padding(horizontal = 2.dp))


                Text(
                    text = device.lastActivity.toString(),
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 17.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight.Normal,
                        color = Inactive2,
                    )
                )
            }

        }

        if (isThisDevice == false){

            IconButton(onClick = onDelete ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = null,
                    tint = Color.White,

                    )
            }
        }

    }
}