package tm.bent.dinle.ui.components.pager

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tm.bent.dinle.domain.model.Banner
import tm.bent.dinle.ui.DinleImage
import tm.bent.dinle.ui.destinations.home.HomeViewModel


@Composable
fun HorizontalPagerView(
    banners:List<Banner>,
    pagerState: PagerState,
    imageHeight: Int,
    onClick : () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp - 20.dp
    val context = LocalContext.current
    val homeViewModel = hiltViewModel<HomeViewModel>()

    Column(modifier = Modifier
        .padding(bottom = 8.dp)
        .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp)
        ) { page ->

            val link = banners[page].link
            val song = banners[page].song
            Box(modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .clickable {
                    if (link != null) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)
                        }catch (e: Exception){
                        }

                    }
                    if (song != null) {
                        try {
                            homeViewModel.getPlayerController().init(0, listOf(song))
                        }catch (e: Exception){

                        }
                    }
                }
                .clip(MaterialTheme.shapes.medium)
                .widthIn(0.dp, screenWidth)
                .height(imageHeight.dp)){
                DinleImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = banners[page].cover
                )
            }



        }
//        PagerIndicator(
//            count = pagerState.pageCount,
//            pagerState = pagerState,
//        )
    }

}

