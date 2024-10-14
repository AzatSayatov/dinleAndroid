package tm.bent.dinle.ui.destinations.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.MainViewModel
import tm.bent.dinle.R
import tm.bent.dinle.ui.DeviceView
import tm.bent.dinle.ui.components.LibraryItemView
import tm.bent.dinle.ui.components.SwitchListItemView
import tm.bent.dinle.ui.components.dialog.ConfirmationDialog
import tm.bent.dinle.ui.components.dialog.LanguageSelectionView
import tm.bent.dinle.ui.destinations.DevicesScreenDestination
import tm.bent.dinle.ui.destinations.LoginScreenDestination
import tm.bent.dinle.ui.destinations.PrivacyScreenDestination
import tm.bent.dinle.ui.theme.Container
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.Inactive3
import tm.bent.dinle.ui.theme.Red
import tm.bent.dinle.ui.theme.RobotoFlex
import tm.bent.dinle.ui.theme.WhiteMilk
import tm.bent.dinle.ui.util.LocaleHelper
import tm.bent.dinle.ui.util.clickWithoutIndication
import tm.bent.dinle.ui.util.findActivity
import tm.bent.dinle.ui.util.restartActivity
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
) {

    val profileViewModel = hiltViewModel<ProfileViewModel>()

    val notification by profileViewModel.notifications.collectAsState(initial = true)
    val phone by profileViewModel.phone.collectAsState(initial = "")
    val lang by profileViewModel.lang.collectAsState(initial = "tk")

    val languageSheetState = rememberModalBottomSheetState()
    val logoutConfirmationDialog = rememberModalBottomSheetState()
    val context = LocalContext.current

    var showLanguage by rememberSaveable { mutableStateOf(false) }
    var showLogoutConfirmation by rememberSaveable { mutableStateOf(false) }
    val hazeState = remember{  }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile),
                        style = TextStyle(
                            fontSize = 22.sp,
                            lineHeight = 26.sp,
                            fontFamily = RobotoFlex,
                            fontWeight = FontWeight(700),
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
//                        modifier = Modifier
//                            .statusBarsPadding()
//                            .legacyBackground
                    )
                }, colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
        ) {


            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Container)
            ) {

                ProfileHeaderView(
                    phone = phone,
                    onclick = {
                        showLogoutConfirmation = true
                    }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background
                )

//                SubscriptionView()
            }

//            Column(
//                modifier = Modifier
//                    .padding(16.dp, 10.dp)
//                    .clip(MaterialTheme.shapes.medium)
//                    .background(Container)
//            ) {
//
//                HorizontalDivider(
//                    color = MaterialTheme.colorScheme.background
//                )
//
//                LibraryItemView(
//                    icon = R.drawable.payment,
//                    title = stringResource(R.string.payment),
//                    subtitle = stringResource(R.string.payed),
//                    extendable = false
//                ) {
//
//                }
//
//
//            }


            Column(
                modifier = Modifier
                    .padding(16.dp, 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Container)
            ) {

//                SwitchListItemView(
//                    icon = R.drawable.ic_notification,
//                    title = stringResource(id = R.string.notification),
//                    isChecked = notification,
//                    onToggle = { checked ->
//                        profileViewModel.toggleNotifications(checked)
//                    }
//                )
//                HorizontalDivider(
//                    color = MaterialTheme.colorScheme.background
//                )

                LibraryItemView(
                    icon = R.drawable.ic_globe,
                    title = stringResource(id = R.string.language),
                    subtitle = stringResource(R.string.langugaes)
                ) {
                    showLanguage = true
                }


            }

            Column(
                modifier = Modifier
                    .padding(16.dp, 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Container)
            ) {

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background
                )

                LibraryItemView(
                    icon = R.drawable.ic_devices,
                    title = stringResource(R.string.devices),
                    subtitle = null,
                    extendable = true
                ) {
                    navigator.navigate(DevicesScreenDestination())
                }


            }

//            Column(
//                modifier = Modifier
//                    .padding(16.dp, 10.dp)
//                    .clip(MaterialTheme.shapes.medium)
//                    .background(Container)
//            ) {
//
//                HorizontalDivider(
//                    color = MaterialTheme.colorScheme.background
//                )
//
//                LibraryItemView(
//                    icon = R.drawable.theme,
//                    title = stringResource(R.string.theme),
//                    subtitle = "Dark",
//                    extendable = true
//                ) {
//
//                }
//
//
//            }

            Column(
                modifier = Modifier
                    .padding(16.dp, 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Container)
            ) {

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background
                )

                LibraryItemView(
                    icon = R.drawable.local_phone,
                    title = stringResource(R.string.call_us),
                    subtitle = null,
                    extendable = false
                ) {
                    val phoneNumber = "tel:+99360120907" // Укажите нужный номер телефона
                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber))

                    // Убедитесь, что у вас есть разрешение на звонок
                    ContextCompat.startActivity(context, callIntent, null)
                }


            }

            Column(
                modifier = Modifier
                    .padding(16.dp, 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Container)
            ) {

//                LibraryItemView(
//                    icon = R.drawable.ic_devices,
//                    title = stringResource(id = R.string.connected_devices),
//                ) {
//                    navigator.navigate(DevicesScreenDestination)
//                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background
                )

                LibraryItemView(
                    icon = R.drawable.ic_privacy,
                    title = stringResource(id = R.string.privacy_policy),
                ) {
                    navigator.navigate(PrivacyScreenDestination)
                }

//                HorizontalDivider(
//                    color = MaterialTheme.colorScheme.background,
//                )
//
//                LibraryItemView(
//                    icon = R.drawable.ic_terms_of_use,
//                    title = stringResource(id = R.string.terms_of_use),
//                ) {
//
//                }
//                HorizontalDivider(
//                    color = MaterialTheme.colorScheme.background,
//                )
//
//                LibraryItemView(
//                    icon = R.drawable.ic_phone,
//                    title = stringResource(id = R.string.contact_us),
//                ) {
//
//                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp, 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Container)
            ) {

                LibraryItemView(
                    icon = R.drawable.ic_logout,
                    title = stringResource(id = R.string.logout),
                    color = Red,
                    extendable = false
                ) {
                    showLogoutConfirmation = true
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Dinle v1.0.0",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Normal,
                    color = Inactive3,
                ),
                textAlign = TextAlign.Center
            )


            Spacer(Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "PRODUCT BY BEREKET BENDI HJ",
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Normal,
                    color = Inactive2,
                ),
                textAlign = TextAlign.Center
            )
        }


        if (showLanguage) {
            LanguageSelectionView(
                selectedLanguage = lang,
                sheetState = languageSheetState,
                onDismissRequest = { showLanguage = false },
                onSelect = { lang ->
                    showLanguage = false
                    LocaleHelper.setLocale(
                        context, lang
                    )
                    restartActivity(context.findActivity())

                },
            )
        }

        if (showLogoutConfirmation) {
            ConfirmationDialog(
                title = stringResource(R.string.do_you_really_want_to_log_out),
                sheetState = logoutConfirmationDialog,
                onConfirm = {
                    showLogoutConfirmation = false
                    profileViewModel.logout(context)
                    profileViewModel.clearUserData()

                },
                onDismissRequest = { showLogoutConfirmation = false }
            )
        }

    }


}

@Composable
fun ProfileHeaderView(
    phone:String,
    onclick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { }
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_account_circle_filled),
            contentDescription = null
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.dinle_id),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight(500),
                    color = MaterialTheme.colorScheme.primary,
                )
            )

            Text(
                text = phone,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Normal,
                    color = Inactive2,
                )
            )

        }
        Icon(modifier = Modifier
            .padding(end = 20.dp)
            .size(24.dp)
            .clickWithoutIndication { onclick() },
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = null,
            tint = WhiteMilk
        )
    }
}


private fun resetApplicationState(context: Context) {
    // Очистка SharedPreferences
    val sharedPreferences = context.getSharedPreferences("your_preferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    // Другие действия для сброса состояния
}

@Preview
@Composable
fun SubscriptionView() {
    Row(
        modifier = Modifier
            .clickable { }
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.subscription),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            )

            Text(
                text = "Tolenen wagty:12.12.2023",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontFamily = RobotoFlex,
                    fontWeight = FontWeight.Normal,
                    color = Inactive2,
                )
            )

        }

        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arr_right),
                contentDescription = null,
                tint = Color.White,

                )
        }
    }
}
