package org.hinsun.music.presentation.introduction

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.design.widgets.base.BaseScaffold

@Composable
fun IntroductionView(navHostController: NavHostController) {

    val context = LocalContext.current
    val fileUri = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .path(R.raw.pianoart_7471283551481253151.toString()).build()

    val mediaItem = MediaItem.fromUri(fileUri)

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            setMediaItem(mediaItem)
            prepare()
        }
    }

    BaseScaffold { innerPadding ->
        AndroidExternalSurface(
            modifier = Modifier
                .padding(innerPadding),
            onInit = {
                onSurface { surface, _, _ ->
                    exoPlayer.volume = 0.4f
                    exoPlayer.setVideoSurface(surface)
                    surface.onDestroyed { exoPlayer.setVideoSurface(null) }
                }
            }
        )
    }
}