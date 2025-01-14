package org.ic.tech.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.ic.tech.music.design.theme.AICMusicTheme
import org.ic.tech.music.presentation.graphs.NavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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