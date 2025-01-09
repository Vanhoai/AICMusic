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
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer

enum class PlayerAction(val value: String) {
    PLAY("play"),
    PAUSE("pause"),
    NEXT("next"),
    PREVIOUS("previous"),
    SEEK_TO("seek-to")
}


class AudioService : Service() {

    private var exoPlayer: ExoPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    override fun onBind(intent: Intent?): IBinder = binder

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