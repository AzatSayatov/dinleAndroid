package tm.bent.dinle.ui.components.pager

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tm.bent.dinle.domain.model.Song

@Composable
fun SongsPager(
    pages: List<List<Song>>,
    onSongClick: (Song) -> Unit,
    onMoreClick: (Song) -> Unit,
    modifier: Modifier = Modifier
){
    val pagerState = rememberPagerState(0,0f, { 0 })



}