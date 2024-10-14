package tm.bent.dinle.ui.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import tm.bent.dinle.ui.NavGraphs
import tm.bent.dinle.ui.appCurrentDestinationAsState
import tm.bent.dinle.ui.destinations.Destination
import tm.bent.dinle.ui.destinations.HomeScreenDestination
import tm.bent.dinle.ui.destinations.LibraryScreenDestination
import tm.bent.dinle.ui.destinations.SearchScreenDestination
import tm.bent.dinle.ui.startAppDestination

import tm.bent.dinle.ui.theme.Inactive
import tm.bent.dinle.ui.theme.Inactive2
import tm.bent.dinle.ui.theme.Inactive3


@Composable
fun BottomBar(
    navController: NavController
) {

    val currentDestination: Destination =
        navController.appCurrentDestinationAsState().value ?: NavGraphs.root.startAppDestination
    var selectedDestination by remember {
        mutableStateOf(HomeScreenDestination.route)
    }

    val destinations = listOf(
        HomeScreenDestination.route,
        SearchScreenDestination.route,
        LibraryScreenDestination.route,
//        ProfileScreenDestination.route
    )

    LaunchedEffect(currentDestination) {
        if (destinations.contains(currentDestination.route)) {
            selectedDestination = currentDestination.route
        }
    }

//    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {


        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            BottomBarDestination.values().forEach { destination ->
                val selected = selectedDestination == destination.direction.route
                val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (isCurrentDestOnBackStack) {
                            // When we click again on a bottom bar item and it was already selected
                            // we want to pop the back stack until the initial destination of this bottom bar item
                            navController.popBackStack(destination.direction, false)
                            return@NavigationBarItem
                        }
                        navController.navigate(destination.direction) {
                            launchSingleTop = true
                            restoreState = true
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                        }

                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.background),
                    icon = {
                        Icon(
                            painterResource(id = destination.icon),
                            contentDescription = destination.name,
                            tint = if (selected) MaterialTheme.colorScheme.onBackground else Inactive2

                        )
                    },
                )
            }
        }
//    }
}
