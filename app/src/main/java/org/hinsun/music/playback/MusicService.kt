package org.hinsun.music.playback

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.audio.SonicAudioProcessor
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.analytics.PlaybackStats
import androidx.media3.exoplayer.analytics.PlaybackStatsListener
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.exoplayer.audio.SilenceSkippingAudioProcessor
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.mkv.MatroskaExtractor
import androidx.media3.extractor.mp4.FragmentedMp4Extractor
import androidx.media3.extractor.text.SubtitleParser
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hinsun.music.CHANNEL_ID
import org.hinsun.music.MainActivity
import org.hinsun.music.R
import org.hinsun.music.di.DownloadCache
import org.hinsun.music.di.PlayerCache
import org.hinsun.music.extensions.SilentHandler
import org.hinsun.music.utilities.CoilBitmapLoader
import timber.log.Timber
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MusicService : MediaLibraryService(),
    Player.Listener,
    PlaybackStatsListener.Callback {

    @Inject
    lateinit var mediaLibrarySessionCallback: MediaLibrarySessionCallback

    private val binder = MusicBinder()
    private var scope = CoroutineScope(Dispatchers.Main + Job())

    lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaLibrarySession

    private var currentQueue: Queue = EmptyMusicQueue

    private val currentTrack = MutableStateFlow<MediaItem?>(null)
    private var playlist = mutableListOf<MediaItem>()
    private val maxDuration = MutableStateFlow(0f)
    private val currentDuration = MutableStateFlow(0f)
    val isPlaying = MutableStateFlow(false)

    @Inject
    @PlayerCache
    lateinit var playerCache: SimpleCache

    @Inject
    @DownloadCache
    lateinit var downloadCache: SimpleCache

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?) = super.onBind(intent) ?: binder

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    private fun createRenderersFactory() =
        object : DefaultRenderersFactory(this) {
            override fun buildAudioSink(
                context: Context,
                enableFloatOutput: Boolean,
                enableAudioTrackPlaybackParams: Boolean,
            ) = DefaultAudioSink.Builder(this@MusicService)
                .setEnableFloatOutput(enableFloatOutput)
                .setEnableAudioTrackPlaybackParams(enableAudioTrackPlaybackParams)
                .setAudioProcessorChain(
                    DefaultAudioSink.DefaultAudioProcessorChain(
                        emptyArray(),
                        SilenceSkippingAudioProcessor(2_000_000, 0.01f, 2_000_000, 0, 256),
                        SonicAudioProcessor()
                    )
                )
                .build()
        }

    @OptIn(UnstableApi::class)
    private fun createCacheDataSource(): CacheDataSource.Factory =
        CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(
                CacheDataSource.Factory()
                    .setCache(playerCache)
            )
            .setCacheWriteDataSinkFactory(null)
            .setFlags(FLAG_IGNORE_CACHE_ON_ERROR)

    private fun createDataSourceFactory(): DataSource.Factory {
        val songUrlCache = HashMap<String, Pair<String, Long>>()
        return ResolvingDataSource.Factory(createCacheDataSource()) { dataSpec ->

            dataSpec.subrange(dataSpec.uriPositionOffset, CHUNK_LENGTH)
        }
    }

    private fun createMediaSourceFactory() =
        DefaultMediaSourceFactory(
            createDataSourceFactory(),
            ExtractorsFactory {
                arrayOf(MatroskaExtractor(), FragmentedMp4Extractor())
            }
        )

    override fun onCreate() {
        super.onCreate()

        // .setMediaSourceFactory(createMediaSourceFactory())
        exoPlayer = ExoPlayer.Builder(this)
            .setRenderersFactory(createRenderersFactory())
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(), true
            )
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
            .apply {
                addListener(this@MusicService)

                Timber.tag("AudioService").d("ExoPlayer Created")

                addAnalyticsListener(PlaybackStatsListener(false, this@MusicService))
            }

        mediaSession = MediaLibrarySession.Builder(this, exoPlayer, mediaLibrarySessionCallback)
            .setSessionActivity(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setBitmapLoader(CoilBitmapLoader(this, scope))
            .build()
    }

    fun playQueue(queue: Queue, playWhenReady: Boolean = true) {
        if (!scope.isActive)
            scope = CoroutineScope(Dispatchers.Main + Job())

        currentQueue = queue
        exoPlayer.shuffleModeEnabled = false

        scope.launch(SilentHandler) {
            if (queue.songs.isEmpty()) return@launch

            exoPlayer.setMediaItems(queue.songs)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = playWhenReady
        }
    }

//    private fun play(track: Track?) {
//        if (playlist.isEmpty()) return
//        val realTrack = track ?: playlist[0]
//
//        try {
//            exoPlayer?.let { player ->
//                player.stop()
//
//                // Create media item with proper resource URI
//                val uri = getRawUri(realTrack.id)
//                val mediaItem = MediaItem.fromUri(uri)
//
//                player.setMediaItem(mediaItem)
//                player.prepare()
//
//                player.playWhenReady = true
//                player.addListener(object : Player.Listener {
//                    override fun onPlaybackStateChanged(playbackState: Int) {
//                        when (playbackState) {
//                            Player.STATE_READY -> {
//                                Timber.tag("AudioService").d("Send notification")
//                                sendNotification(realTrack)
//                                updateDuration()
//                                Timber.tag("AudioService").d("Track ready: ${realTrack.name}")
//                            }
//
//                            Player.STATE_ENDED -> {
//                                Timber.tag("AudioService").d("Track ended: ${realTrack.name}")
//                            }
//
//                            else -> {
//                                val error = player.playerError
//                                Timber.tag("AudioService").e("Playback error: $error")
//                            }
//                        }
//                    }
//                })
//            }
//        } catch (e: Exception) {
//            Timber.tag("AudioService").e("Error playing track: ${e.message}")
//        }
//    }

    // Optional: Helper function to check if resource exists
    private fun isResourceExists(resourceId: Int): Boolean {
        return try {
            resources.getResourceName(resourceId)
            true
        } catch (e: Resources.NotFoundException) {
            false
        }
    }

//    private fun sendNotification(track: Track) {
//        isPlaying.update { exoPlayer.isPlaying }
//        val style = androidx.media.app.NotificationCompat.MediaStyle()
//            .setShowActionsInCompactView(0, 1, 2)
//
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setStyle(style)
//            .setContentTitle(track.name)
//            .setContentText(track.desc)
//            .addAction(R.drawable.prev, "prev", createPrevPendingIntent())
//            .addAction(
//                if (exoPlayer.isPlaying) R.drawable.pause else R.drawable.play,
//                "play",
//                createPlayPendingIntent()
//            )
//            .addAction(R.drawable.next, "next", createNextPendingIntent())
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.onboard_1))
//            .build()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                startForeground(1, notification)
//            }
//        } else {
//            startForeground(1, notification)
//        }
//    }

    private fun createPrevPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            action = PREV
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            action = NEXT
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createPlayPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            action = PLAY
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
    
    companion object {
        const val PREV = "prev"
        const val NEXT = "next"
        const val PLAY = "play"

        const val ERROR_CODE_NO_STREAM = 1000001
        const val CHUNK_LENGTH = 512 * 1024L
        const val PERSISTENT_QUEUE_FILE = "persistent_queue.data"
    }

    override fun onPlaybackStateChanged(@Player.State playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        Timber.tag("AudioService").d("Playback State: $playbackState")
    }

    override fun onPlaybackStatsReady(
        eventTime: AnalyticsListener.EventTime,
        playbackStats: PlaybackStats
    ) {
        val mediaItem =
            eventTime.timeline.getWindow(eventTime.windowIndex, Timeline.Window()).mediaItem
        Timber.tag("AudioService").d("Media Item: $mediaItem")
    }
}