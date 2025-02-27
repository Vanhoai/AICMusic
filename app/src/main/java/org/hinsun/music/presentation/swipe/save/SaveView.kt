package org.hinsun.music.presentation.swipe.save

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.hinsun.music.R
import org.hinsun.music.core.ui.design.theme.AppTheme
import org.hinsun.music.core.ui.design.widgets.base.BaseButton
import org.hinsun.music.core.ui.design.widgets.shared.SharedCardSong
import org.hinsun.music.core.ui.design.widgets.shared.SharedGradientButton
import org.hinsun.music.core.ui.design.widgets.shared.SharedGradientOutlineImage
import org.hinsun.music.core.ui.design.widgets.shared.SharedRowText
import org.hinsun.music.presentation.swipe.save.widgets.DownloadAction
import org.hinsun.music.presentation.swipe.save.widgets.DownloadModalBottomSheet
import org.hinsun.music.presentation.swipe.save.widgets.PasteLink
import org.hinsun.music.core.services.DownloadState
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SaveView(
    navHostController: NavHostController,
    saveViewModel: SaveViewModel = hiltViewModel<SaveViewModel>()
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var showBottomSheet by remember { mutableStateOf(false) }

    var url by remember { mutableStateOf("") }
    var jobId by remember { mutableStateOf("") }

    val downloadMap by saveViewModel.downloadState.collectAsState()
    val progressState by saveViewModel.progress.collectAsState()
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(downloadMap) {
        if (downloadMap[jobId] != null) {
            val downloadState = downloadMap[jobId]
            if (downloadState != null && downloadState is DownloadState.Downloading) {
                progress = downloadState.progress
            }

            Timber.tag("SaveView").d("progress: $progress")
        }
    }

    LaunchedEffect(progressState) {
        progress = progressState
        Timber.tag("SaveView").d("progress: $progress")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(scrollState, orientation = Orientation.Vertical)
            .clickable { keyboardController?.hide() }
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                )

                SharedGradientOutlineImage()
            }
        }

        item {
            PasteLink(onDownload = {
                showBottomSheet = true
                url = it
            })
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            DownloadAction()
        }

        item {
            Text(
                text = "Just now",
                style = AppTheme.typography.normal,
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
            )

            SharedCardSong()
        }

        item {
            SharedRowText(text = "Downloaded")
        }

        items(10) {
            SharedCardSong()
        }
    }

    DownloadModalBottomSheet(
        progress = progress,
        showBottomSheet = showBottomSheet,
        onCloseBottomSheet = {
            showBottomSheet = false
        },
        onStart = {
            val job = saveViewModel.startDownload(url)
            if (job != null) jobId = job
        }
    )
}

