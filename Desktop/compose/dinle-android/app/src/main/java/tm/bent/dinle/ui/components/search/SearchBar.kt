package tm.bent.dinle.ui.components.search

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Surface2
import tm.bent.dinle.ui.theme.WhiteMilk
import tm.bent.dinle.ui.util.clickWithoutIndication


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    search: String = "",
    onFocusChanged: (Boolean) -> Unit = {},
    onDone: (String) -> Unit
) {


    var text by rememberSaveable { mutableStateOf(search) }
    val interactionSource = remember { MutableInteractionSource() }
    val trailingIcon =  R.drawable.ic_close


    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .focusRequester(focusRequester)
            .clearFocusOnKeyboardDismiss()
            .onFocusChanged { focused -> onFocusChanged(focused.isFocused) }
        ,
        value = text,
        onValueChange = { text = it },
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = RobotoFlex,
            fontWeight = FontWeight(500),
            color = Inactive,
        ),
        cursorBrush = SolidColor(Color.White),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onDone(text.trim())
        }),
    ) {
        TextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = it,
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(500),
                        color = Inactive,
                    )
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Inactive,
                focusedContainerColor = Surface2,
                unfocusedContainerColor = Surface2,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = WhiteMilk

                )
            },
            trailingIcon = {
                if (text.isNotEmpty()){
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickWithoutIndication { text = "" },
                        painter = painterResource(id = trailingIcon),
                        contentDescription = null,
                        tint = WhiteMilk

                    )
                }

            },

            shape = MaterialTheme.shapes.small,

            enabled = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        )
    }
}
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {

    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        val isKeyboardOpen by rememberIsKeyboardOpen()

        val focusManager = LocalFocusManager.current
        LaunchedEffect(isKeyboardOpen) {
            if (isKeyboardOpen) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}
@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.isKeyboardOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen() }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener)  }
    }
}
fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect);
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom;
    return keypadHeight > screenHeight * 0.15
}
