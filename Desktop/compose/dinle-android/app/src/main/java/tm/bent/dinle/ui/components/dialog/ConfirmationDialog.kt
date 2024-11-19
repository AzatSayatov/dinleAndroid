package tm.bent.dinle.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.theme.Container
import tm.bent.dinle.ui.theme.RobotoFlex


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    title: String,
    sheetState: SheetState,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.padding(bottom = bottomPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontFamily = RobotoFlex,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp, 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Container)
            ) {

                OptionView(
                    text = stringResource(id = R.string.yes),
                    onClick = onConfirm
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background
                )

                OptionView(
                    text = stringResource(id = R.string.no),
                    onClick = onDismissRequest
                )
            }


        }
    }

}


@Composable
fun OptionView(
    text: String,
    onClick: () -> Unit,

    ) {
    Text(modifier = Modifier
        .clickable { onClick() }
        .padding(20.dp)
        .fillMaxWidth(),
        text = text,
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 16.8.sp,
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(400),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start
        ))
}

@Composable
fun ButtonView(
    text: String,
    onClick: () -> Unit,

    ) {
    Text(modifier = Modifier
        .clickable { onClick() }
        .padding(20.dp),
        text = text,
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 16.8.sp,
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(400),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start
        ))
}