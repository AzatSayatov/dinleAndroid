package tm.bent.dinle.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LoadingView(
    modifier: Modifier,
) {
    Box(
        modifier.padding(top = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}