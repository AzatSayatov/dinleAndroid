package tm.bent.dinle.ui.destinations.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.ui.theme.Border
import tm.bent.dinle.ui.theme.Primary2


@Composable
fun CharView(
    isTextFieldFocused: Boolean,
    isError: Boolean,
    index: Int,
    text: String
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidth = (screenWidth - 120.dp) / 6
    val isFocused = text.length == index  && isTextFieldFocused
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }


    val border = if (isError) Primary2 else if (isFocused || char.isNotEmpty() ) Primary2 else Border

    val background =  MaterialTheme.colorScheme.background
    Text(
        modifier = Modifier
            .width(itemWidth)
            .background(
                background, shape = MaterialTheme.shapes.medium
            )
            .border(1.dp, border, MaterialTheme.shapes.medium)
            .padding(14.dp),
        text = char,
        color = MaterialTheme.colorScheme.onBackground,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        textAlign = TextAlign.Center
    )
}