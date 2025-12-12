package org.hinsun.music.presentation.onboard

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hinsun.music.R
import org.hinsun.music.constants.IsFirstLaunchKey
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.design.widgets.shared.SharedGradientButton
import org.hinsun.music.extensions.rememberPreference
import org.hinsun.music.presentation.graphs.NavRoute
import org.hinsun.music.presentation.onboard.widgets.DualOrbitingArcs
import org.hinsun.music.presentation.onboard.widgets.OnboardHeading
import timber.log.Timber

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OnBoardView(
    navHostController: NavHostController,
    viewModel: OnBoardViewModel = hiltViewModel<OnBoardViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val sizeBox = (screenWidth - 40 - 8) / 2

    val (isFirstLaunch, onChangeIsFirstLaunch) = rememberPreference(IsFirstLaunchKey, true)

    val context = LocalContext.current
    val fileUri = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .path(R.raw.onboard.toString()).build()


    val mediaItem = MediaItem.fromUri(fileUri)
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            setMediaItem(mediaItem)
            prepare()
        }
    }

    LaunchedEffect(Unit) {
        exoPlayer.playWhenReady = true
    }

    val coroutine = rememberCoroutineScope()
    val onPress = remember {
        {
            coroutine.launch {
                onChangeIsFirstLaunch(false)

                exoPlayer.stop()
                exoPlayer.release()

                delay(500)
                navHostController.navigate(NavRoute.AUTH.path)
            }
        }
    }

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            OnboardHeading(sizeBox)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DualOrbitingArcs()
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Music",
                style = AppTheme.typography.normal,
                fontWeight = FontWeight.Black,
                fontSize = 100.sp,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            SharedGradientButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                onPress = {
                    onPress()
                }
            ) {
                Text(
                    text = "Get Started",
                    style = AppTheme.typography.normal,
                    fontSize = 20.sp,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}
