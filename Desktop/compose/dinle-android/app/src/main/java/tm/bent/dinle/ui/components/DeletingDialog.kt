package tm.bent.dinle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.ui.theme.RobotoFlex
@Composable
fun DeletingDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {



    AlertDialog(
        modifier = Modifier
            .background(Color.Black),
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontFamily = RobotoFlex,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка "Hawa"
                Box(
                    modifier = Modifier
                        .clickable { onConfirm() }
                        .weight(1f)
                        .height(50.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp)) // Добавляем тень
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Hawa",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        fontFamily = RobotoFlex
                    )
                }

                Spacer(modifier = Modifier.width(16.dp)) // Добавляем пространство между кнопками

                // Кнопка "Yok"
                Box(
                    modifier = Modifier
                        .clickable { onDismissRequest() }
                        .weight(1f)
                        .height(50.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp)) // Добавляем тень
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0Xff101010)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Yok",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        fontFamily = RobotoFlex
                    )
                }
            }
        },
//        dismissButton = {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp)
//                    .background(Color.Black)
//                    .height(50.dp)
//                    .clip(RoundedCornerShape(20.dp)),
//                contentAlignment = Alignment.Center
//            ) {
//                Button(
//                    onClick = { onDismissRequest() },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(20.dp)
//                ) {
//                    Text(
//                        text = "Yok",
//                        color = Color.White,
//                        fontFamily = RobotoFlex,
//                        fontWeight = FontWeight(400),
//                        fontSize = 14.sp
//                    )
//                }
//            }
//        }
    )
}
