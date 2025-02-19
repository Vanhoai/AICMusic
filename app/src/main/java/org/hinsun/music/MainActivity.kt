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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hinsun.music.design.theme.AICMusicTheme
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.design.widgets.providers.SharedLoadingProvider
import org.hinsun.music.presentation.graphs.NavGraph
import org.hinsun.music.playback.MusicService
import org.hinsun.music.playback.Track
import org.hinsun.music.playback.tracks
import timber.log.Timber

@OptIn(UnstableApi::class)
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val isPlaying = MutableStateFlow(false)
    private val maxDuration = MutableStateFlow(0f)
    private val currentDuration = MutableStateFlow(0f)
    private val currentTrack = MutableStateFlow<Track?>(null)

    private var service: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as MusicService.MusicBinder).getService()
            lifecycleScope.launch {
                binder.isPlaying().collectLatest {
                    isPlaying.value = it
                }
            }

            lifecycleScope.launch {
                binder.maxDuration().collectLatest {
                    maxDuration.value = it
                }
            }

            lifecycleScope.launch {
                binder.currentDuration().collectLatest {
                    currentDuration.value = it
                }
            }

            lifecycleScope.launch {
                binder.currentTrack().collectLatest {
                    currentTrack.value = it
                }
            }

            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
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
        Timber.tag("AudioService").d("Stop Service")
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("AudioService").d("On Destroy")

        if (isFinishing) {
            stopService(Intent(this, MusicService::class.java))
            unbindService(serviceConnection)
            service = null
        }
    }

    @OptIn(UnstableApi::class)
    @kotlin.OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AICMusicTheme {
//                val navHostController = rememberNavController()
//                SharedLoadingProvider {
//                    Box(modifier = Modifier.fillMaxSize()) {
//                        NavGraph(navHostController)
//                    }
//                }

                Scaffold(
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            val track = currentTrack.collectAsState().value
                            val max = maxDuration.collectAsState().value
                            val current = currentDuration.collectAsState().value
                            val isPlaying = isPlaying.collectAsState().value

                            Image(
                                painter = painterResource(
                                    id = track?.image ?: R.drawable.ic_launcher_background
                                ),
                                contentDescription = null,
                                modifier = Modifier.height(300.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = track?.name ?: "Nothing",
                                style = AppTheme.typography.normal,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = current.div(1000).toString(),
                                    style = AppTheme.typography.normal,
                                    fontSize = 16.sp
                                )

                                Slider(
                                    modifier = Modifier.weight(1f),
                                    value = current,
                                    onValueChange = {},
                                    valueRange = 0f..max,
                                )

                                Text(
                                    text = max.div(1000).toString(),
                                    style = AppTheme.typography.normal,
                                    fontSize = 16.sp
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                IconButton(onClick = {
                                    service?.prev()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.prev),
                                        contentDescription = null
                                    )
                                }

                                IconButton(onClick = {
                                    service?.playPause()
                                }) {
                                    Icon(
                                        painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                                        contentDescription = null
                                    )
                                }

                                IconButton(onClick = {
                                    service?.next()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.next),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

