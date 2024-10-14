package tm.bent.dinle.ui.destinations.otp

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import tm.bent.dinle.R
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.Red
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Surface
import tm.bent.dinle.ui.util.clickWithoutIndication


@Destination
@RootNavGraph
@Composable
fun DeviceOtpScreen(
    deviceId: String,
    phone: String,
    navigator: DestinationsNavigator,
) {
    val verificationViewModel = hiltViewModel<VerificationViewModel>()


    LaunchedEffect(deviceId) {
        verificationViewModel.removeDevice(deviceId)
    }


    val uiState by verificationViewModel.uiState.collectAsState()

    val (code, onCodeChange) = remember {
        mutableStateOf("")
    }
    val (isFocused, onFocusChange) = remember {
        mutableStateOf(false)
    }

    val (isError, onErrorChange) = remember {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.failure){
        if (uiState.failure){
            scope.launch {
                snackbarHostState.showSnackbar(
                    uiState.errorMessage
                )
            }

            verificationViewModel.updateToDefault()
        }

    }

    LaunchedEffect(uiState.success){
        if (uiState.success){
            navigator.navigateUp()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Surface,
                    contentColor = Color.Red,
                    snackbarData = data
                )
            }
        },
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            },
        topBar = {
            SimpleTopAppBar(title = "") {
                navigator.navigateUp()
            }
        }) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(start = 20.dp, end = 20.dp, top = 130.dp)
                .padding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.enter_verification_code),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 20.dp),
                text = stringResource(R.string.enter_verification_code_desc, phone),
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(400),
                    color = Inactive,
                    textAlign = TextAlign.Center,
                )
            )


            BasicTextField(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .onFocusChanged {
                        onFocusChange(it.isFocused)
                    },
                value = code,
                onValueChange = {
                    if (it.length > 4) return@BasicTextField
                    onCodeChange(it)
                    if (it.length == 4) verificationViewModel.removeDeviceOtp(phone, it.toInt())

                },
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        repeat(4) { index ->
                            CharView(
                                isTextFieldFocused = isFocused,
                                isError = isError,
                                index = index,
                                text = code
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                },

                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()

                }),
            )

            if (isError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 40.dp),
                    text = stringResource(R.string.invalid_code_desc),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.8.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(400),
                        color = Red,
                        textAlign = TextAlign.Center,
                    )
                )

            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier.clickWithoutIndication {
                    verificationViewModel.removeDevice(deviceId)
                },
                text = stringResource(R.string.resend_code),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(400),
                    color = Inactive2,
                    textAlign = TextAlign.Center,
                )
            )

        }

        if (uiState.loading) {
            LoadingView(
                Modifier.fillMaxSize()
            )
        }


    }
}