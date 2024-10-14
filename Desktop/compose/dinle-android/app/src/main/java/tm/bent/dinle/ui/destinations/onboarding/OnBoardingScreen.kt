package tm.bent.dinle.ui.destinations.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.R
import tm.bent.dinle.ui.components.CustomButton
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.Primary1
import tm.bent.dinle.ui.theme.Surface2
import tm.bent.dinle.ui.theme.WhiteMilk


@Composable
fun OnBoardingScreen(
    onPass: () ->Unit
) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .padding(horizontal = 40.dp)
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.img_onboarding),
                contentDescription = null,
                contentScale = ContentScale.Crop,

                )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                text = stringResource(R.string.enjoy_every_moment_wit_dinle),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(700)
                ),
                textAlign = TextAlign.Center,

                )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = stringResource(R.string.enjoy_every_moment_wit_dinle_desc),
                style = TextStyle(
                    color = Inactive, fontSize = 12.sp, fontWeight = FontWeight(500)
                ),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text = stringResource(id = R.string.dinle_slogan),
                onClick = onPass,
                containerColor = Primary1,
                shape = MaterialTheme.shapes.small,
                contentColor = MaterialTheme.colorScheme.onBackground
            )


        }
    }


}
