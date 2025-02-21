package org.hinsun.music.presentation.swipe.save.widgets

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadModalBottomSheet(
    progress: Float = 0f,
    onStart: () -> Unit,
    showBottomSheet: Boolean = false,
    onCloseBottomSheet: () -> Unit = {},
) {
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    fun closeBottomSheet() {
        scope.launch(Dispatchers.Main) {
            sheetState.hide()
            onCloseBottomSheet()
        }
    }

    fun startDownload() {
        onStart()
//        scope.launch {
//            animate(
//                initialValue = 0f,
//                targetValue = 100f,
//                animationSpec = tween(
//                    durationMillis = 5000,
//                    easing = LinearEasing
//                )
//            ) { value, _ ->
//                progress = value
//                if (value >= 100f) {
//                    Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = ::closeBottomSheet,
            sheetState = sheetState,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFD9D9D9))
                )
            },
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            containerColor = Color(0xFF464646),
        ) {
            DownloadProgress(
                downloadProgress = progress,
                onStartDownload = ::startDownload,
                onCancel = ::closeBottomSheet,
                onOk = ::closeBottomSheet
            )
        }
    }
}