package org.hinsun.music

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.hinsun.music.design.theme.AICMusicTheme
import org.hinsun.music.design.widgets.providers.SharedLoadingProvider
import org.hinsun.music.presentation.graphs.NavGraph

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AICMusicTheme {
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