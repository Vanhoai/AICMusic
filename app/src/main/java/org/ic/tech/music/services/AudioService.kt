package org.ic.tech.music.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer

enum class PlayerAction(val value: String) {
    PLAY("play"),
    PAUSE("pause"),
    NEXT("next"),
    PREVIOUS("previous"),
    SEEK_TO("seek-to")
}

@UnstableApi
class AudioService : Service() {

    private var exoPlayer: ExoPlayer? = null
    private var isExoPlayerReady: Boolean = false
    private var exoPlayerPrepare: ExoPlayer? = null
    private var isExoPlayerPrepareReady: Boolean = false
    private lateinit var mediaSession: MediaSessionCompat

    private var receiver: NotificationActionReceiver = NotificationActionReceiver()
    private var receiverRegistered: Boolean = false

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    // Create your Player and MediaSession in the onCreate lifecycle event
    @SuppressLint("InlinedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "AudioService")
        mediaSession.setCallback(
            object : MediaSessionCompat.Callback() {

                override fun onPlay() {
                    Log.d(TAG, "onPlay")
                    super.onPlay()
                    mediaSession.isActive = true
                    playAudio()
                }

                override fun onPause() {
                    Log.d(TAG, "onPause")
                    super.onPause()
                    pauseAudio()
                }

                override fun onSkipToPrevious() {
                    Log.d(TAG, "onSkipToPrevious")
                    super.onSkipToPrevious()
                    backAudio()
                }

                override fun onSkipToNext() {
                    Log.d(TAG, "onSkipToNext")
                    super.onSkipToNext()
                    nextAudio()
                }
            },
        )
        mediaSession.isActive = true

        registerReceiver(
            receiver, IntentFilter(ACTION_NOTIFICATION_BUTTON_CLICK),
            RECEIVER_NOT_EXPORTED
        )

        receiverRegistered = true

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(1000, 5000, 1000, 1000)
            .setTargetBufferBytes(C.LENGTH_UNSET)
            .build()
        exoPlayer = ExoPlayer.Builder(this).setLoadControl(loadControl).build()
        exoPlayerPrepare = ExoPlayer.Builder(this).setLoadControl(loadControl).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY
        when (intent.getStringExtra("command")) {
            commandPrepare -> {
                val url = intent.getStringExtra("url") ?: return START_NOT_STICKY
                prepareURL(url)
            }

            commandExecute -> {
                val url = intent.getStringExtra("url") ?: return START_NOT_STICKY
                val details = intent.getStringExtra("details") ?: return START_NOT_STICKY
                type = intent.getStringExtra("type") ?: ""
                executeURL(url, details)
            }

            commandPlay -> {
                LogUtils.d("C_LOG", "commandPlay: ${exoPlayer?.isPlaying}")
                playAudio()
            }

            commandPause -> {
                pauseAudio()
            }

            commandBack -> {
                backAudio()
            }

            commandNext -> {
                nextAudio()
            }

            commandSetVolume -> {
                val volume = intent.getFloatExtra("volume", -1f)
                if (volume in 0f..1f) {
                    setPlayerVolume(volume)
                }
            }

            commandStop -> {
                stopAudio()
            }

            commandSeekTo -> {
                val position = intent.getLongExtra("position", -1L)
                if (position != -1L) {
                    doSeekTo(position)
                }
            }

            commandRegisterMediaStateChanged -> {
                registerMediaStateChanged()
            }

            commandUnregisterMediaStateChanged -> {
                unregisterMediaStateChanged()
            }
        }
        return START_NOT_STICKY
    }

        return START_NOT_STICKY
    }

    private inner class NotificationActionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive")
            if (intent == null) return
            when (intent.getIntExtra(EXTRA_BUTTON_CLICKED, -1)) {
                ACTION_BACK -> {
                    back()
                }

                ACTION_PAUSE -> {
                    val state = intent.getIntExtra(EXTRA_CURRENT_STATE, -1)
                    if (state == PlaybackStateCompat.STATE_PLAYING) {
                        pause()
                    } else {
                        play()
                    }
                }

                ACTION_NEXT -> {
                    next()
                }
            }
        }
    }

    companion object {
        private val TAG = AudioService::class.java.name

        const val ACTION_NOTIFICATION_BUTTON_CLICK = "ACTION_NOTIFICATION_BUTTON_CLICK"
        const val EXTRA_BUTTON_CLICKED = "extraButtonClicked"
        const val EXTRA_CURRENT_STATE = "extraCurrentState"
        const val ACTION_BACK = 1
        const val ACTION_PAUSE = 2
        const val ACTION_NEXT = 3
    }
}