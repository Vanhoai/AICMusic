package org.hinsun.music.playback

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

val LocalPlayerConnection =
    staticCompositionLocalOf<PlayerConnection?> { error("No PlayerConnection provided") }

@OptIn(UnstableApi::class)
class PlayerConnection(
    context: Context,
    binder: MusicService.MusicBinder,
    scope: CoroutineScope,
) : Player.Listener {
    val service = binder.getService()
    val exoPlayer = service.exoPlayer

    val playbackState = MutableStateFlow(exoPlayer.playbackState)
    private val playWhenReady = MutableStateFlow(exoPlayer.playWhenReady)
    val isPlaying = combine(playbackState, playWhenReady) { playbackState, playWhenReady ->
        playWhenReady && playbackState != STATE_ENDED
    }.stateIn(
        scope,
        SharingStarted.Lazily,
        exoPlayer.playWhenReady && exoPlayer.playbackState != STATE_ENDED
    )

    init {
        exoPlayer.addListener(this)

        playbackState.value = exoPlayer.playbackState
        playWhenReady.value = exoPlayer.playWhenReady
    }

    fun playQueue(queue: Queue) {
        service.playQueue(queue)
    }

    override fun onPlaybackStateChanged(state: Int) {
        playbackState.value = state
        super.onPlaybackStateChanged(state)
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        Timber.tag("AudioService").d("Timeline Changed $timeline.")
        super.onTimelineChanged(timeline, reason)
    }

    fun dispose() = exoPlayer.removeListener(this)
}





