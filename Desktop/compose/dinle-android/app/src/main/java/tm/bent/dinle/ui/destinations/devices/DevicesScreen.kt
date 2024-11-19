package tm.bent.dinle.ui.destinations.devices

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.domain.model.Device
import tm.bent.dinle.ui.DeviceView
import tm.bent.dinle.ui.components.LoadingView
import tm.bent.dinle.ui.components.NoConnectionView
import tm.bent.dinle.ui.components.SimpleTopAppBar
import tm.bent.dinle.ui.components.dialog.ConfirmationDialog
import tm.bent.dinle.ui.components.pullrefresh.PullRefreshIndicator
import tm.bent.dinle.ui.components.pullrefresh.rememberPullRefreshState
import tm.bent.dinle.ui.destinations.DeviceOtpScreenDestination

import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.RobotoFlex

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun DevicesScreen(
    navigator: DestinationsNavigator,
) {
    val deviceViewModel = hiltViewModel<DeviceViewModel>()

    LaunchedEffect(Unit) {
        deviceViewModel.fetchDevices()
    }

    val uiState by deviceViewModel.uiState.collectAsState()
    val phone by deviceViewModel.phone.collectAsState("")
    val devices by deviceViewModel.devices.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { deviceViewModel.fetchDevices() }
    )
    val confirmationDialog = rememberModalBottomSheetState()

    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var selectedDevice: Device? = null

    Scaffold(modifier = Modifier, topBar = {
        SimpleTopAppBar(title = stringResource(id = R.string.devices)) {
            navigator.navigateUp()
        }
    }) { padding ->

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
        ) {
            if (uiState.isLoading) {
                LoadingView(Modifier.fillMaxSize())
            } else if (uiState.isFailure) {
                NoConnectionView(Modifier.fillMaxSize()) {
                    deviceViewModel.fetchDevices()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.current_device).uppercase(),
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontFamily = RobotoFlex,
                                fontWeight = FontWeight.Normal,
                                color = Inactive2,
                            )
                        )

                    }
                    item {
                        DeviceView(deviceViewModel.getDeviceInfo(),
                            isThisDevice = true,
                            onDelete = {})
                        Spacer(Modifier.height(20.dp))
                    }
                    if (devices.size -1 != 0){
                        item {
                            Text(
                                text = stringResource(R.string.devices).uppercase(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = RobotoFlex,
                                    fontWeight = FontWeight.Normal,
                                    color = Inactive2,
                                )
                            )

                        }
                    }

                    items(devices.filter { it.id != Build.ID }){ device ->
                        DeviceView(device = device,
                            isThisDevice = false,
                            onDelete = {
                                selectedDevice = device
                            showConfirmationDialog = true})
                    }



                }

            }
            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                pullRefreshState,
            )

            if (showConfirmationDialog) {
                ConfirmationDialog(
                    title = stringResource(R.string.do_you_really_want_to_log_out),
                    sheetState = confirmationDialog,
                    onConfirm = {
                        showConfirmationDialog = false
                        navigator.navigate(DeviceOtpScreenDestination(phone = phone, deviceId = selectedDevice!!.id))
                    },
                    onDismissRequest = { showConfirmationDialog = false }
                )
            }
        }

    }
}