package tm.bent.dinle.ui.components

import androidx.compose.animation.core.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

@Composable
fun GreyShimmer(
    content: @Composable (Brush) -> Unit
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.onBackground.copy(0.05f),
        MaterialTheme.colorScheme.onBackground.copy(0.15f),
        MaterialTheme.colorScheme.onBackground.copy(0.05f)
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1600f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = 0f)
    )

    content(brush)
}