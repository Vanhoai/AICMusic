package org.ic.tech.music.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession

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
        exoPlayer = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, exoPlayer!!).build()

        Toast.makeText(this, "AudioService Created", Toast.LENGTH_SHORT).show()
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        exoPlayer?.release()
        mediaSession?.release()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
}