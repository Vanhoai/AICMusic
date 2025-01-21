package org.hinsun.music

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.hinsun.music.design.theme.AICMusicTheme
import org.hinsun.music.presentation.graphs.NavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AICMusicTheme {
                val navHostController = rememberNavController()
                NavGraph(navHostController)
            }
        }
    }
}