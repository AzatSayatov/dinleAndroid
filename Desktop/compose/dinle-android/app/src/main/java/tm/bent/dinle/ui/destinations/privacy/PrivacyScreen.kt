package tm.bent.dinle.ui.destinations.privacy

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.components.SimpleTopAppBar


@Destination
@Composable
fun PrivacyScreen(
    navigator: DestinationsNavigator
) {

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            Column {
                SimpleTopAppBar(title = stringResource(id = R.string.privacy_policy)) {
                    navigator.navigateUp()
                }
            }
        },
    ) { padding ->

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()

                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.setSupportZoom(true)
                    loadUrl("https://dinle.com.tm/api/privacy")
                }

            },
            update = { webView ->
                webView.loadUrl("https://dinle.com.tm/api/privacy")
            }
        )
    }
}