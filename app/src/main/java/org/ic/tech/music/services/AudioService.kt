package org.ic.tech.music.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media3.common.C
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import org.ic.tech.music.CHANNEL_ID
import org.ic.tech.music.R
import org.json.JSONObject

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

    private val receiver = NotificationActionReceiver()
    private var receiverRegistered = false

    private val notificationId = System.currentTimeMillis().toInt()
    private var currentDetails: JSONObject? = null

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this@AudioService, "AudioService")
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                Log.d(TAG, "onPlay")
                super.onPlay()
            }

            override fun onPause() {
                Log.d(TAG, "onPause")
                super.onPause()
            }

            override fun onSkipToPrevious() {
                Log.d(TAG, "onSkipToPrevious")
                super.onSkipToPrevious()
            }

            override fun onSkipToNext() {
                Log.d(TAG, "onSkipToNext")
                super.onSkipToNext()
            }
        })

        mediaSession.isActive = true

        registerReceiver(receiver, IntentFilter(ACTION_NOTIFICATION_BUTTON_CLICK))
        receiverRegistered = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY

        when (intent.action) {
            PlayerAction.PLAY.value -> {
                Log.d(TAG, "onStartCommand: play")
                pushNotification()
            }

            PlayerAction.PAUSE.value -> {
                Log.d(TAG, "onStartCommand: pause")
            }

            PlayerAction.NEXT.value -> {
                Log.d(TAG, "onStartCommand: next")
            }

            PlayerAction.PREVIOUS.value -> {
                Log.d(TAG, "onStartCommand: previous")
            }

            PlayerAction.SEEK_TO.value -> {
                Log.d(TAG, "onStartCommand: seek-to")
            }
        }

        return START_NOT_STICKY
    }

    private fun pushNotification() {
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(0, 1, 2)

        val backIntent = Intent(this, NotificationActionReceiver::class.java)
        backIntent.putExtra(EXTRA_BUTTON_CLICKED, ACTION_BACK)
        val backPendingIntent = PendingIntent.getBroadcast(
            this,
            ACTION_BACK,
            backIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val pauseIntent = Intent(this, NotificationActionReceiver::class.java)
        pauseIntent.putExtra(EXTRA_BUTTON_CLICKED, ACTION_PAUSE)
        currentDetails?.let { pauseIntent.putExtra(EXTRA_CURRENT_STATE, it.getInt("state")) }
        val pausePendingIntent = PendingIntent.getBroadcast(
            this,
            ACTION_PAUSE,
            pauseIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val nextIntent = Intent(this, NotificationActionReceiver::class.java)
        nextIntent.putExtra(EXTRA_BUTTON_CLICKED, ACTION_NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(
            this,
            ACTION_NEXT,
            nextIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("I'am a UITer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("This is a notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.beautiful_crush))
            .addAction(R.drawable.prev, "Previous", backPendingIntent)
            .addAction(R.drawable.play, "Play", pausePendingIntent)
            .addAction(R.drawable.next, "Next", nextPendingIntent)
            .setStyle(mediaStyle)

        startForeground(notificationId, notification.build())
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    inner class NotificationActionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            Log.d(TAG, "onReceive action: ${intent.action}")
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