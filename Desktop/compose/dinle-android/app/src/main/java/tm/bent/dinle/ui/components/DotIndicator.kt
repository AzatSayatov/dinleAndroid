package tm.bent.dinle.ui.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import tm.bent.dinle.ui.theme.Border
import tm.bent.dinle.ui.theme.Primary1


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicator(
    size :Int,
    pagerState: PagerState
) {
    Canvas(modifier = Modifier
        .width(40.dp)
        .height(6.dp)) {
        val spacing = 5.dp.toPx()
        val dotWidth = 6.dp.toPx()
        val dotHeight = 6.dp.toPx()

        val activeDotWidth = 14.dp.toPx()
        var x = 0f
        val y = center.y

        repeat(size) { i ->
            val posOffset = pagerState.pageOffset
            val dotOffset = posOffset % 1
            val current = posOffset.toInt()

            val factor = (dotOffset * (activeDotWidth - dotWidth))

            val calculatedWidth = when {
                i == current -> activeDotWidth - factor
                i - 1 == current || (i == 0 && posOffset > size - 1) -> dotWidth + factor
                else -> dotWidth
            }

            drawIndicator(x, y, calculatedWidth, dotHeight, CornerRadius(100f, 100f), Primary1 )
            x += calculatedWidth + spacing
        }
    }
}

private fun DrawScope.drawIndicator(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: CornerRadius,
    color: Color
) {
    val rect = RoundRect(
        x,
        y - height / 2,
        x + width,
        y + height / 2,
        radius
    )

    val path = Path().apply { addRoundRect(rect) }
    drawPath(path = path, color = color)
}

// To get scroll offset
@OptIn(ExperimentalFoundationApi::class)
val PagerState.pageOffset: Float
    get() = this.currentPage + this.currentPageOffsetFraction


// To get scrolled offset from snap position
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}
