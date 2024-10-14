package tm.bent.dinle.ui.destinations.artists

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import tm.bent.dinle.domain.model.Artist
import tm.bent.dinle.domain.model.BaseRequest
import tm.bent.dinle.ui.components.ArtistRowView
import tm.bent.dinle.ui.destinations.ArtistScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SubscribedArtistsScreen(navigator: DestinationsNavigator){

    val context = LocalContext.current
    val artistPreferences = ArtistsPreferences(context)
    val subscribedArtists = remember { mutableStateListOf<Artist>() }

    // Загрузка подписанных артистов из SharedPreferences
    LaunchedEffect(Unit) {
        subscribedArtists.addAll(artistPreferences.getSubscribedArtists())
    }

    LazyColumn {
        items(subscribedArtists) { artist ->
           ArtistRowView(artist = artist) {
               navigator.navigate(ArtistScreenDestination(baseRequest = BaseRequest(artistId = artist.id)))
           }
        }
    }
}
