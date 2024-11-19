package tm.bent.dinle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.theme.RobotoFlex

@Composable
fun NotFoundView(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 30.dp)
            ,
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Image(
                painter = painterResource(id = R.drawable.ic_campaign),
                contentDescription = "image description",
                contentScale = ContentScale.None
            )

            Text(
                modifier = Modifier.padding(vertical = 13.dp),
                text = stringResource(R.string.not_found),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.2.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = Color.White,

                    )
            )


        }


    }

}
