package tm.bent.dinle.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Primary1 = Color(0xFFE80944).copy(0.5f)
val Primary2 = Color(0xFFFF3740)
val Orange2_33 = Color(0x33F16820)
val OrangeBorder = Color(0x80FD9900)
val OrangeDark = Color(0xFF431700)
val Inactive = Color(0xFFC1C1C1)
val Inactive2 = Color(0xFF5E5E62)
val Button1 = Color(0xFF151515)
val Inactive3 = Color(0xFFB3B3B3)
val Border = Color(0xFF252525)
val Divider = Color(0xFF141414)
val Black80 = Color(0x80000000)
val Black33 = Color(0x33000000)
val Surface = Color(0xFF0B0B0B)
val Surface2 = Color(0xFF131313)
val BackgroundSecond = Color(0xFF030303)
val Background = Color(0xFF090909)
val BlackBg = Color(0xFF0A0A0A)
val Red = Color(0xFFF12020)
val Red33 = Color(0x33F12020)
val BackgroundGray = Color(0x0D000000)
val OnBackgroundLight = Color(0xFF333333)
val WhiteMilk = Color(0xFFFCFCFD)
val Subtitle = Color(0xFFA19D9D)
val Orange = Color(0xFFF16820)
val Purple = Color(0xFF68026A)
val Blue = Color(0xFF0F60FF)
val ButtonBackground = Color(0x80111111)
val Container = Color(0xff0d0d0d)
val InactiveBorder = Color(0x1A5E5E62)

val primaryGradient = Brush.verticalGradient(
    colors = listOf(
        Primary1,
        Primary2
    )
)
val premiumGradient = Brush.linearGradient(
    colors = listOf(
        Blue,
        Purple,
        Orange
    ),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

val orangeGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFEEEDEC),
        Color(0xFFFB6C62),
        Color(0xFFEEEDEC),
        Color(0xFFEEEDEC),
    ),
)
val backgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Background, Black80, Black80, Background
    ),
)