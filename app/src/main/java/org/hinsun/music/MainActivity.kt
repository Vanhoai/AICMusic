package org.hinsun.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.hinsun.music.core.database.LocalDatabase
import org.hinsun.music.core.database.MusicDatabase
import org.hinsun.music.core.playback.LocalPlayerConnection
import org.hinsun.music.core.playback.MusicService
import org.hinsun.music.core.playback.PlayerConnection
import org.hinsun.music.core.ui.design.theme.AICMusicTheme
import org.hinsun.music.core.ui.design.widgets.providers.SharedLoadingProvider
import org.hinsun.music.presentation.graphs.NavGraph
import javax.inject.Inject

@OptIn(UnstableApi::class)
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var database: MusicDatabase

    private var service: MusicService? = null
    private var playerConnection by mutableStateOf<PlayerConnection?>(null)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as MusicService.MusicBinder).getService()
            playerConnection = PlayerConnection(this@MainActivity, binder, lifecycleScope)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playerConnection?.dispose()
            playerConnection = null
        }
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this, MusicService::class.java))
        bindService(
            Intent(this, MusicService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            stopService(Intent(this, MusicService::class.java))
            unbindService(serviceConnection)
            service = null
        }
    }

    @OptIn(UnstableApi::class)
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AICMusicTheme {
                CompositionLocalProvider(
                    LocalPlayerConnection provides playerConnection,
                    LocalDatabase provides database
                ) {
                    val navHostController = rememberNavController()
                    SharedLoadingProvider {
                        Box(modifier = Modifier.fillMaxSize()) {
                            NavGraph(navHostController)
                        }
                    }
                }
            }
        }
    }
}