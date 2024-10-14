package tm.bent.dinle.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.ui.theme.RobotoFlex


@Composable
fun CustomButton(
    modifier: Modifier,
    text: String,
    onClick:()->Unit,
    containerColor: Color,
    contentColor: Color,
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
    shape: Shape = MaterialTheme.shapes.large
) {
    Button(
        modifier = modifier,
        shape = shape,
        onClick = onClick,
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 25.dp),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = containerColor,
            disabledContainerColor = containerColor,
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            leadingIcon?.let {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    tint = contentColor,
                    contentDescription = null
                )
            }


            Text(
                text =  text,
                color = contentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                fontFamily = RobotoFlex,
            )

            trailingIcon?.let {
                Icon(
                    painter = painterResource(id = trailingIcon),
                    tint = contentColor,
                    contentDescription = null
                )
            }
        }

    }
}