package tm.bent.dinle

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import tm.bent.dinle.domain.model.BaseRequest

import tm.bent.dinle.ui.destinations.onboarding.OnBoardingScreen
import tm.bent.dinle.ui.destinations.player.MiniPlayer
import tm.bent.dinle.ui.destinations.player.PlayerScreen

import tm.bent.dinle.ui.navigation.screen.BottomBar

import tm.bent.dinle.ui.theme.DinleTheme
import tm.bent.dinle.ui.util.LocaleHelper
import tm.bent.dinle.ui.NavGraphs
import tm.bent.dinle.ui.appCurrentDestinationAsState
import tm.bent.dinle.ui.destinations.ArtistScreenDestination
import tm.bent.dinle.ui.destinations.Destination
import tm.bent.dinle.ui.destinations.HomeScreenDestination
import tm.bent.dinle.ui.destinations.LoginScreenDestination
import tm.bent.dinle.ui.destinations.SongInfoScreenDestination
import tm.bent.dinle.ui.destinations.VideoPlayerScreenDestination
import tm.bent.dinle.ui.destinations.downloads.DownloadsViewModel
import tm.bent.dinle.ui.startAppDestination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainViewModel: MainViewModel by viewModels()


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "tk"))
    }

    @OptIn(
        ExperimentalMaterialNavigationApi::class,
        ExperimentalAnimationApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)

        setContent {

            val scope = rememberCoroutineScope()
            val token by mainViewModel.token.collectAsState(initial = null)
            Log.i("MainActivity", "onCreate: $token")
            val firstTime by mainViewModel.firstTime.collectAsState(initial = null)
            var showPlayer by remember { mutableStateOf(false) }

            DinleTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {


                    val navController = rememberNavController()
                    val navLoginController = rememberNavController()
                    val startRoute = NavGraphs.root.startRoute
                    val destination = navController.appCurrentDestinationAsState().value
                        ?: startRoute.startAppDestination

                    val songViewModel = hiltViewModel<DownloadsViewModel>()
                    val songs by songViewModel.getDownloadedSongs().collectAsState(initial = emptyList())


                    Scaffold(
                        bottomBar = {
                            if (destination.shouldShowScaffoldElements(
                                    token?.isNotEmpty() == true,
                                    firstTime == true
                                )
                            ) {
                                Column {
                                    mainViewModel.getPlayerController().selectedTrack?.let { song ->

                                        MiniPlayer(
                                            song = song,
                                            playerController = mainViewModel.getPlayerController(),
                                            onClick = {
                                                showPlayer = true
                                            },

                                            )
                                    }
                                    BottomBar(navController)

                                }
                            }


                        }
                    ) { padding ->
                        if (token?.isNotEmpty() == true) {



                            DestinationsNavHost(
                                navController = navController,
                                navGraph = NavGraphs.root,
                                modifier = Modifier
                                    .padding(bottom = padding.calculateBottomPadding())
                                    .fillMaxSize(),
                                startRoute = HomeScreenDestination,
                                engine = rememberAnimatedNavHostEngine()
                            )
                            if (firstTime == true) {
                                OnBoardingScreen(
                                    onPass = {
                                        mainViewModel.passOnboarding()
                                    }
                                )
                            }

                            if (showPlayer) {
                                PlayerScreen(
                                    playerController = mainViewModel.getPlayerController(),
                                    downloadTracker = mainViewModel.getDownloadTracker(),
                                    onLike = { id ->
                                        mainViewModel.likeSong(id)
                                    },
                                    onNavigateToInfo = {
                                        showPlayer = false
                                        mainViewModel.getPlayerController().selectedTrack?.id?.let {
                                            navController.navigate(
                                                SongInfoScreenDestination(id = it)
                                            )
                                        }
                                    },
                                    onNavigateToArtist = { id ->
                                        showPlayer = false
                                        navController.navigate(
                                            ArtistScreenDestination(BaseRequest(artistId = id))
                                        )
                                    },
                                    onListen = { id ->
                                        mainViewModel.listen(id)
                                    },
                                    onDownload = { song ->
                                        val isInArray = song in songs
                                        if (!isInArray){
                                            Log.e("TAG", "onCreate: "+song)
                                            mainViewModel.insertSong(song)
                                            mainViewModel.listen(song.id, true)
                                        }


                                    },
                                    onDismiss = {
                                        showPlayer = false
                                    },
                                )

                            }
                        } else if (token?.isEmpty() == true) {
                            DestinationsNavHost(
                                navController = navLoginController,
                                navGraph = NavGraphs.login,
                                modifier = Modifier
                                    .padding(bottom = padding.calculateBottomPadding())
                                    .fillMaxSize(),
                                startRoute = LoginScreenDestination,
                            )
                        }
                    }

                }
            }
        }
    }

}

private fun Destination.shouldShowScaffoldElements(loggedIn: Boolean, firstTime: Boolean): Boolean {
//    this !=
    return !firstTime && loggedIn && this.route != VideoPlayerScreenDestination.route
}

