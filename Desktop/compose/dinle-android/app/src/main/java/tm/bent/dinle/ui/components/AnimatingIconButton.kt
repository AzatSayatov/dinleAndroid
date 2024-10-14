package tm.bent.dinle.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tm.bent.dinle.R
import tm.bent.dinle.ui.theme.OrangeBorder
import tm.bent.dinle.ui.theme.primaryGradient


@Composable
fun AnimatingIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )
    Box( modifier = Modifier
        .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {

        Spacer(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin.Center
                }
                .size(50.0.dp)
                .border(0.2.dp, OrangeBorder, CircleShape)
                .padding(5.dp)
                .border(0.2.dp, OrangeBorder, CircleShape)
                .padding(5.dp)



        )

        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .background(primaryGradient),
            onClick = onClick,
        ) {
            Icon(
                tint = Color.White,
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    }


}

@Preview
@Composable
fun dfa() {
    AnimatingIconButton(R.drawable.ic_globe){

    }
}