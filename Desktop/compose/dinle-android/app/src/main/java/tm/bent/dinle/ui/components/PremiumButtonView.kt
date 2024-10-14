package tm.bent.dinle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.R
import tm.bent.dinle.ui.theme.orangeGradient
import tm.bent.dinle.ui.theme.premiumGradient


@Composable
fun PremiumButtonView(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(
                brush = premiumGradient,
                shape = MaterialTheme.shapes.medium,
                alpha = 0.5f
            )
            .padding(10.dp, 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_premium),
            contentDescription = stringResource(id = R.string.premium),
            tint = Color.White
        )

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.premium),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight(700),

                ),
        )

        Button(
            modifier = Modifier
                .background(
                    brush = orangeGradient,
                    shape = MaterialTheme.shapes.medium,
                    alpha = 0.5f,
                ).height(30.dp),
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(33.dp, 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.buy),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                ),
            )
        }

    }

}