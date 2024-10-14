package tm.bent.dinle.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import tm.bent.dinle.R

// Set of Material typography styles to start with
/*val Typography = Typography(
   bodyLarge = TextStyle(
       fontFamily = FontFamily.Default,
       fontWeight = FontWeight.Normal,
       fontSize = 16.sp,
       lineHeight = 24.sp,
       letterSpacing = 0.5.sp
   )
 OtherDefault text styles to override
   titleLarge = TextStyle(
       fontFamily = FontFamily.Default,
       fontWeight = FontWeight.Normal,
       fontSize = 22.sp,
       lineHeight = 28.sp,
       letterSpacing = 0.sp
   ),
   labelSmall = TextStyle(
       fontFamily = FontFamily.Default,
       fontWeight = FontWeight.Medium,
       fontSize = 11.sp,
       lineHeight = 16.sp,
       letterSpacing = 0.5.sp
   )

)
*/

val RobotoFlex = FontFamily(
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = RobotoFlex),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = RobotoFlex),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = RobotoFlex),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = RobotoFlex),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = RobotoFlex),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = RobotoFlex),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = RobotoFlex),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = RobotoFlex),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = RobotoFlex),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = RobotoFlex),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = RobotoFlex),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = RobotoFlex),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = RobotoFlex),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = RobotoFlex),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = RobotoFlex)
)