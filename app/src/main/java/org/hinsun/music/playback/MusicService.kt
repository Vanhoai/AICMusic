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
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
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
import kotlinx.coroutines.launch
import org.hinsun.music.CHANNEL_ID
import org.hinsun.music.R
import org.hinsun.music.di.DownloadCache
import org.hinsun.music.di.PlayerCache
import timber.log.Timber
import javax.inject.Inject

@UnstableApi
class MusicService : Service() {

    private val binder = MusicBinder()
    private var exoPlayer: ExoPlayer? = null

    private val currentTrack = MutableStateFlow<Track?>(null)
    private var playlist = mutableListOf<Track>()
    private val maxDuration = MutableStateFlow(0f)
    private val currentDuration = MutableStateFlow(0f)
    val isPlaying = MutableStateFlow(false)

    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

    @Inject
    @PlayerCache
    lateinit var playerCache: SimpleCache

    @Inject
    @DownloadCache
    lateinit var downloadCache: SimpleCache

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService

        fun currentDuration() = this@MusicService.currentDuration

        fun maxDuration() = this@MusicService.maxDuration

        fun isPlaying() = this@MusicService.isPlaying

        fun currentTrack() = this@MusicService.currentTrack
    }

    override fun onBind(intent: Intent?) = binder

    @OptIn(UnstableApi::class)
    private fun createExoPlayer(context: Context): ExoPlayer {
        // Create a custom RenderersFactory
        val renderersFactory = DefaultRenderersFactory(context).apply {
            setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
        }

        // Create and configure custom Extractor Factory
        val extractorsFactory = DefaultExtractorsFactory().apply {
            setConstantBitrateSeekingEnabled(true)
        }

        // Create custom media source factory
        val mediaSourceFactory = DefaultMediaSourceFactory(
            createDataSourceFactory()
        ) {
            arrayOf(MatroskaExtractor(), FragmentedMp4Extractor())
        }

        // Build ExoPlayer with all configurations
        return ExoPlayer.Builder(context)
            .setRenderersFactory(createRenderersFactory())
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
    }

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

    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                PREV -> prev()
                NEXT -> next()
                PLAY -> playPause()
                else -> {
                    setPlaylist(tracks)
                    currentTrack.update { playlist[0] }
                    play(playlist[0])
                }
            }
        }

        return START_STICKY
    }

    private fun updateDuration() {
        job = scope.launch {
            if (exoPlayer!!.isPlaying.not()) return@launch

            Timber.tag("AudioService").d("Update duration ${exoPlayer!!.duration}")
            maxDuration.update { exoPlayer!!.duration.toFloat() }

            while (true) {
                currentDuration.update { exoPlayer!!.currentPosition.toFloat() }
                delay(1000)
            }
        }
    }

    fun prev() {
        job?.cancel()
        exoPlayer!!.stop()

        val index = playlist.indexOf(currentTrack.value!!)
        val prevIndex = if (index < 0) playlist.size.minus(1) else index.minus(1)
        val prevTrack = playlist[prevIndex]

        currentTrack.update { prevTrack }
        val uri = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .path(prevTrack.id.toString()).build()
        exoPlayer!!.setMediaItem(MediaItem.fromUri(uri))
        exoPlayer!!.prepare()

        exoPlayer!!.play()
        sendNotification(prevTrack)
    }

    fun next() {
        job?.cancel()
        exoPlayer!!.stop()

        val index = playlist.indexOf(currentTrack.value!!)
        val nextIndex = index.plus(1).mod(playlist.size)
        val nextTrack = playlist[nextIndex]

        currentTrack.update { nextTrack }
        val uri = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .path(nextTrack.id.toString()).build()
        exoPlayer!!.setMediaItem(MediaItem.fromUri(uri))
        exoPlayer!!.prepare()

        exoPlayer!!.play()
        sendNotification(nextTrack)
        updateDuration()
    }

    fun playPause() {
        if (exoPlayer!!.isPlaying) {
            exoPlayer!!.pause()
        } else {
            exoPlayer!!.play()
        }

        sendNotification(currentTrack.value!!)
    }

    fun setPlaylist(songs: List<Track>) {
        this.playlist = songs.toMutableList()
    }

    private fun play(track: Track?) {
        if (playlist.isEmpty()) return
        val realTrack = track ?: playlist[0]

        try {
            exoPlayer?.let { player ->
                player.stop()

                // Create media item with proper resource URI
                val uri = getRawUri(realTrack.id)
                val mediaItem = MediaItem.fromUri(uri)

                player.setMediaItem(mediaItem)
                player.prepare()

                player.playWhenReady = true
                player.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_READY -> {
                                Timber.tag("AudioService").d("Send notification")
                                sendNotification(realTrack)
                                updateDuration()
                                Timber.tag("AudioService").d("Track ready: ${realTrack.name}")
                            }

                            Player.STATE_ENDED -> {
                                Timber.tag("AudioService").d("Track ended: ${realTrack.name}")
                            }

                            else -> {
                                val error = player.playerError
                                Timber.tag("AudioService").e("Playback error: $error")
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            Timber.tag("AudioService").e("Error playing track: ${e.message}")
        }
    }

    private fun getRawUri(resourceId: Int): Uri {
        return Uri.parse("android.resource://$packageName/$resourceId")
    }

    // Optional: Helper function to check if resource exists
    private fun isResourceExists(resourceId: Int): Boolean {
        return try {
            resources.getResourceName(resourceId)
            true
        } catch (e: Resources.NotFoundException) {
            false
        }
    }

    // Optional: Extension function to safely create MediaItem
    private fun Track.toMediaItem(context: Context): MediaItem? {
        return try {
            val uri = getRawUri(this.id)
            if (isResourceExists(this.id)) {
                MediaItem.fromUri(uri)
            } else {
                Timber.tag("AudioService").e("Resource not found: ${this.id}")
                null
            }
        } catch (e: Exception) {
            Timber.tag("AudioService").e("Error creating MediaItem: ${e.message}")
            null
        }
    }

    private fun sendNotification(track: Track) {
        isPlaying.update { exoPlayer!!.isPlaying }
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(track.name)
            .setContentText(track.desc)
            .addAction(R.drawable.prev, "prev", createPrevPendingIntent())
            .addAction(
                if (exoPlayer!!.isPlaying) R.drawable.pause else R.drawable.play,
                "play",
                createPlayPendingIntent()
            )
            .addAction(R.drawable.next, "next", createNextPendingIntent())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.onboard_1))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(1, notification)
            }
        } else {
            startForeground(1, notification)
        }
    }

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
}