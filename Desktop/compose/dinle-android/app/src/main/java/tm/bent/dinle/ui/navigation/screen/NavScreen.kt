package tm.bent.dinle.ui.navigation.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import tm.bent.dinle.hinlen.R
import tm.bent.dinle.ui.destinations.HomeScreenDestination
import tm.bent.dinle.ui.destinations.LibraryScreenDestination
import tm.bent.dinle.ui.destinations.SearchScreenDestination


enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    Home(HomeScreenDestination, R.string.main,R.drawable.ic_home),
    Search(SearchScreenDestination, R.string.search, R.drawable.ic_search),
    Library(LibraryScreenDestination, R.string.library, R.drawable.ic_playlist),
//    Profile(ProfileScreenDestination, R.string.profile, R.drawable.ic_account_circle),
}

@NavGraph
annotation class LoginNavGraph(
    val start: Boolean = false
)