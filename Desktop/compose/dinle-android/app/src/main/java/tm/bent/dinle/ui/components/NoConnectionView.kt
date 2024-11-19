package tm.bent.dinle.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Surface2


@Composable
fun NoConnectionView(
    modifier: Modifier = Modifier,
    onRefresh: () ->Unit
) {


        Column(
            modifier = modifier
                .padding(30.dp),
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Image(
                painter = painterResource(id = R.drawable.ic_wifi_off),
                contentDescription = "image description",
                contentScale = ContentScale.None
            )

            Text(
                modifier = Modifier.padding(vertical = 20.dp),
                text = stringResource(R.string.no_connection),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.2.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = Color.White,

                    )
            )

            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
                text = stringResource(id = R.string.refresh),
                onClick = onRefresh,
                trailingIcon = R.drawable.ic_refresh,
                containerColor = Surface2,
                contentColor = MaterialTheme.colorScheme.onBackground
            )

        }





}

@Preview
@Composable
fun dfsdf() {
    NoConnectionView(

    ){}

}