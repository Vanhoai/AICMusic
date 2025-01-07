package org.ic.tech.music.services

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import org.ic.tech.music.CHANNEL_ID
import org.ic.tech.music.R

enum class PlayerAction(val value: String) {
    PLAY_PAUSE("play_pause"),
    NEXT("next"),
    PREVIOUS("previous"),
}

class AudioService : Service() {

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    // Create your Player and MediaSession in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "AudioService Created", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                PlayerAction.PLAY_PAUSE.value -> {
                    Log.d("AudioService", "Play/Pause")
                }

                PlayerAction.NEXT.value -> {
                    sendNotification(songs[0])
                }

                PlayerAction.PREVIOUS.value -> {
                    Log.d("AudioService", "Previous")
                }

                else -> {
                    Log.d("AudioService", "Unknown action")
                }
            }
        }

        return START_NOT_STICKY
    }

    private fun sendNotification(track: Track) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(track.name)
            .setContentText(track.desc)
            .addAction(R.drawable.next, "Next", createNextPendingIntent())
            .addAction(R.drawable.prev, "Previous", createPrevPendingIntent())
            .addAction(
                if (exoPlayer?.isPlaying == true) R.drawable.play else R.drawable.pause,
                if (exoPlayer?.isPlaying == true) "Pause" else "Play",
                createPlayPausePendingIntent()
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
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
        val intent = Intent(this, AudioService::class.java).apply {
            action = PlayerAction.PREVIOUS.value
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, AudioService::class.java).apply {
            action = PlayerAction.NEXT.value
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createPlayPausePendingIntent(): PendingIntent {
        val intent = Intent(this, AudioService::class.java).apply {
            action = PlayerAction.PLAY_PAUSE.value
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}