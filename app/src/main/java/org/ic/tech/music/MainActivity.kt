package org.ic.tech.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import org.ic.tech.music.services.AudioService
import org.ic.tech.music.ui.theme.AICMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AICMusicTheme {
                MainComposable()
            }
        }
    }
}

@Composable
fun MainComposable() {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                startMusicService(context)
            }) {
                Text(text = "Play")
            }

            Button(onClick = {
                stopMusicService(context)
            }) {
                Text(text = "Pause")
            }
        }
    }
}

fun startMusicService(context: Context) {
    val intent = Intent(context, AudioService::class.java)
    ContextCompat.startForegroundService(context, intent)
}

fun stopMusicService(context: Context) {
    val intent = Intent(context, AudioService::class.java)
    context.stopService(intent)
}