package tm.bent.dinle.ui.destinations.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import tm.bent.dinle.R
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.destinations.OTPScreenDestination
import tm.bent.dinle.ui.destinations.devices.DeviceViewModel
import tm.bent.dinle.ui.navigation.screen.LoginNavGraph
import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.OnBackgroundLight
import tm.bent.dinle.ui.theme.Primary2
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.Surface


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@LoginNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
) {

    val loginViewModel = hiltViewModel<LoginViewModel>()
    val phone by loginViewModel.phone.collectAsState("")

    val deviceViewModel = hiltViewModel<DeviceViewModel>()

    val uiStateDevice by deviceViewModel.uiState.collectAsState()
    val devices by deviceViewModel.devices.collectAsState()

    val uiState by loginViewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    val isPhoneNumberValid = phone.isNotEmpty() && phone.length >= 8

    var isTextFieldError by remember {
        mutableStateOf(false)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(uiState.failure){
        if (uiState.failure){
            scope.launch {
                snackbarHostState.showSnackbar(
                    uiState.errorMessage
                )
            }

            loginViewModel.updateToDefault()
        }

    }

    LaunchedEffect(uiState.success){
        if (uiState.success){
            navigator.navigate(OTPScreenDestination("+993$phone"))
            loginViewModel.updateToDefault()

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

        ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom

        ) {

            Box (
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ){
                Image(
                    modifier = Modifier.height(50.dp),
                    painter = painterResource(id = R.drawable.img_logo),
                    contentDescription = "image description",
                )
            }


            Text(
                text = stringResource(R.string.phone_number),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(600),
                    color = Color.White,

                    )
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = {
                    if (it.length <= 8) {
                        loginViewModel.setPhoneNumber(it)
                    }
                },
                leadingIcon = {

                    Row(
                        Modifier
                            .padding(start = 14.dp)
                            .height(IntrinsicSize.Min)

                    ) {
                        Text(
                            text = "+993",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                fontFamily = RobotoFlex
                            ),
                            fontWeight = FontWeight(400),
                            color = Inactive,

                            )
                        Divider(
                            color = Inactive,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxHeight()
                                .width(1.dp)
                        )

                    }


                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(400),
                    color = Inactive,
                ),

                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Inactive,
                    focusedTextColor = Inactive,
                    disabledTextColor = Inactive,
                    focusedBorderColor = OnBackgroundLight,
                    unfocusedBorderColor = OnBackgroundLight,
                    disabledBorderColor = OnBackgroundLight,

                    ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Phone
                ),
                shape = MaterialTheme.shapes.small,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()

                }),
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (devices.size >= 3) {
                Text(
                    text = "You have 3 registered devices.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(600),
                        color = Color.Red,
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                onClick = {
                    loginViewModel.loginUser(phone)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary2,
                    contentColor = Color.White
                ),
                enabled = phone.trim().length == 8,
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(vertical = 14.dp, horizontal = 40.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.continue_string),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontFamily = RobotoFlex,
                        fontWeight = FontWeight(600),
                        color = Color.White,
                    )
                )

            }


        }

        if (uiState.loading) {
            LoadingView(
                Modifier.fillMaxSize()
            )
        }

    }

}
