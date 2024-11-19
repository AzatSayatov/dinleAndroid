    package tm.bent.dinle.player


    import android.content.Context
    import android.media.AudioManager
    import android.util.Log
    import androidx.media3.common.MediaItem
    import androidx.media3.common.PlaybackException
    import androidx.media3.common.Player
    import androidx.media3.exoplayer.ExoPlayer
    import tm.bent.dinle.player.PlayerStates.STATE_BUFFERING
    import tm.bent.dinle.player.PlayerStates.STATE_END
    import tm.bent.dinle.player.PlayerStates.STATE_ERROR
    import tm.bent.dinle.player.PlayerStates.STATE_IDLE
    import tm.bent.dinle.player.PlayerStates.STATE_NEXT_TRACK
    import tm.bent.dinle.player.PlayerStates.STATE_PAUSE
    import tm.bent.dinle.player.PlayerStates.STATE_PLAYING
    import tm.bent.dinle.player.PlayerStates.STATE_READY
    import kotlinx.coroutines.flow.MutableStateFlow
    import tm.bent.dinle.DinleApplication
    import tm.bent.dinle.di.AudioPlayer
    import javax.inject.Inject

    /**
     * A custom player class that provides several convenience methods for
     * controlling playback and monitoring the state of an underlying ExoPlayer.
     *
     * @param player The ExoPlayer instance that this class wraps.
     */
    class MyPlayer @Inject constructor(@AudioPlayer  private val player: ExoPlayer) : Player.Listener {

        /**
         * A state flow that emits the current playback state of the player.
         */
        val playerState = MutableStateFlow(STATE_IDLE)

        /**
         * The current playback position in milliseconds. If the player's position
         * is negative, this returns 0.
         */
        val currentPlaybackPosition: Long
            get() = if (player.currentPosition > 0) player.currentPosition else 0L

        /**
         * The duration of the current track in milliseconds. If the track's duration
         * is negative, this returns 0.
         */
        val currentTrackDuration: Long
            get() = if (player.duration > 0) player.duration else 0L




        private val audioManager = DinleApplication.getAppContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

        private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    if (player.isPlaying) {
                        player.pause()
                    }
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    if (player.isPlaying) {
                        player.pause()
                    }
                }
                AudioManager.AUDIOFOCUS_GAIN -> {
                    if (!player.isPlaying) {
                        player.play()
                    }
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    if (player.isPlaying) {
                        player.stop()
                    }
                }
            }
        }

        // Запрос фокуса аудио
        fun requestAudioFocus() {
            val result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN // Этот флаг позволяет приглушить другие приложения
            )

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                player.play()
            }else{
                player.pause()
            }
        }

        // Освобождение фокуса
        fun abandonAudioFocus() {
            audioManager.abandonAudioFocus(audioFocusChangeListener)
        }

        /**
         * Initializes the player with a list of media items.
         *
         * @param trackList The list of media items to play.
         */
        fun iniPlayer(trackList: MutableList<MediaItem>) {
            requestAudioFocus()
            player.addListener(this)
            player.setMediaItems(trackList)
            player.prepare()
            if (!player.isPlaying) player.play()

        }

        fun addMediaItem(index: Int, track: MediaItem) {
            player.addMediaItem(index, track)


        }
        fun isPlaying(): Boolean{
            return player.isPlaying
        }

        val isShuffleModeEnabled: Boolean
            get() = player.shuffleModeEnabled

        // Функция для включения/выключения shuffle mode
        fun setShuffleModeEnabled(enabled: Boolean) {
            player.shuffleModeEnabled = enabled
        }

        // Получение состояния shuffle mode
        fun getShuffleModeEnabled(): Boolean {
            return player.shuffleModeEnabled
        }


        fun reOrder(from: Int, to: Int, track: MediaItem) {
            player.removeMediaItem(from)
            player.addMediaItem(to, track)

        }

        fun setRepeatMode(mode: Int) {
            player.repeatMode = mode

        }

        fun getRepeatMode(): Int {
            return player.repeatMode
        }


        fun getPlayer(): ExoPlayer {
            return player
        }

        /**
         * Sets up the player to start playback of the track at the specified index.
         *
         * @param index The index of the track in the playlist.
         * @param isTrackPlay If true, playback will start immediately.
         */
        fun setUpTrack(index: Int, isTrackPlay: Boolean) {
            if (player.playbackState == Player.STATE_IDLE) player.prepare()
            player.seekTo(index, 0)
            if (isTrackPlay) player.playWhenReady = true

        }

        /**
         * Toggles the playback state between playing and paused.
         */
        fun playPause() {
            // Сначала запрашиваем аудиофокус

            val result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN // Этот флаг позволяет приглушить другие приложения
            )
            // Если фокус получен, проверяем состояние плеера
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                when {
                    // Если плеер в состоянии idle (не подготовлен), подготавливаем его
                    player.playbackState == Player.STATE_IDLE -> player.prepare()

                    // Если плеер уже воспроизводит, ставим на паузу
                    player.isPlaying -> player.pause()

                    // Если плеер на паузе или остановлен, начинаем воспроизведение
                    else -> player.play()
                }
            } else {
                // Если фокус не получен, то ставим плеер на паузу
                player.pause()
            }
        }



        /**
         * Releases the player, freeing any resources it holds.
         */
        fun releasePlayer() {
            abandonAudioFocus()
            player.release()
        }

        /**
         * Seeks to the specified position in the current track.
         *
         * @param position The position to seek to, in milliseconds.
         */
        fun seekToPosition(position: Long) {
            player.seekTo(position)
        }


        fun emitPlaying() {
            playerState.tryEmit(STATE_PLAYING)
        }
        // Overrides for Player.Listener follow...

        /**
         * Called when a player error occurs. This implementation emits the
         * STATE_ERROR state to the playerState flow.
         */
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.e("TAG", "onPlayerError: " + error.message)
            playerState.tryEmit(STATE_ERROR)
        }

        /**
         * Called when the player's playWhenReady state changes. This implementation
         * emits the STATE_PLAYING or STATE_PAUSE state to the playerState flow
         * depending on the new playWhenReady state and the current playback state.
         */
        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (player.playbackState == Player.STATE_READY) {
                if (playWhenReady) {
                    playerState.tryEmit(STATE_PLAYING)
                } else {
                    playerState.tryEmit(STATE_PAUSE)
                }
            }
        }


        /**
         * Called when the player transitions to a new media item. This implementation
         * emits the STATE_NEXT_TRACK and STATE_PLAYING states to the playerState flow
         * if the transition was automatic.
         */
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {

                playerState.tryEmit(STATE_NEXT_TRACK)
                Log.e("TAG", "onMediaItemTransition: ")
    //            playerState.tryEmit(STATE_PLAYING)


            }
        }

        /**
         * Called when the player's playback state changes. This implementation emits
         * a state to the playerState flow corresponding to the new playback state.
         */
        override fun onPlaybackStateChanged(playbackState: Int) {

            Log.e("TAG", "onPlaybackStateChanged: " + playbackState)
            when (playbackState) {

                Player.STATE_IDLE -> {
                    playerState.tryEmit(STATE_IDLE)
                }

                Player.STATE_BUFFERING -> {
                    playerState.tryEmit(STATE_BUFFERING)
                }

                Player.STATE_READY -> {
                    playerState.tryEmit(STATE_READY)
                    if (player.playWhenReady) {
                        playerState.tryEmit(STATE_PLAYING)
                    } else {
                        playerState.tryEmit(STATE_PAUSE)
                    }
                }

                Player.STATE_ENDED -> {
                    playerState.tryEmit(STATE_END)
                }
            }
        }
    }