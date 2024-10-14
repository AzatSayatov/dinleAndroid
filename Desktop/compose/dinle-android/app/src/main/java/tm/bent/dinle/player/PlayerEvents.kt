package tm.bent.dinle.player

import tm.bent.dinle.domain.model.Song


interface PlayerEvents {

    fun onPlayPauseClick()

    fun onPreviousClick()

    fun onNextClick()

    fun onTrackClick(song: Song)

    fun onPlayNext(song: Song)

    fun onTrackClick(song: Int)

    fun onSeekBarPositionChanged(position: Long)
}
