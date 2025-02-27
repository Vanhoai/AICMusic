package org.hinsun.music.core.ui.design.widgets.providers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.hinsun.music.core.ui.design.widgets.base.BaseLoading

class GlobalLoadingProvider {
    private val _isGlobalLoading = mutableStateOf(false)

    val isGlobalLoading: Boolean
        @Composable
        get() = _isGlobalLoading.value

    fun setGlobalLoading(isLoading: Boolean) {
        _isGlobalLoading.value = isLoading
    }
}

val LocalGlobalLoadingState = compositionLocalOf { GlobalLoadingProvider() }

/**
 * Global Loading Provider
 *
 * This provider is used to show loading animation when the app is loading data from the server.
 * It can be used in any composable, such as a screen or a component.
 *
 * @param content The content to be displayed while the app is loading data.
 * @return A composable that displays the loading animation and the content.
 *
 * @sample
 * SharedLoadingProvider {
 *     // Your content here
 * }
 *
 * @note This provider have a bug, everywhere emit value true or false multiple times it will be
 * rendered multiple times.
 */
@Composable
fun SharedLoadingProvider(content: @Composable () -> Unit) {
    val loadingStateHolder = remember { GlobalLoadingProvider() }
    val isGlobalLoading by rememberUpdatedState(loadingStateHolder.isGlobalLoading)

    CompositionLocalProvider(LocalGlobalLoadingState provides loadingStateHolder) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()

            // Render multiple times if emit sample value true or false multiple times
            AnimatedVisibility(
                visible = isGlobalLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        )
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                }
                            }
                        }
                        .zIndex(Float.MAX_VALUE),
                    contentAlignment = Alignment.Center
                ) {
                    BaseLoading(modifier = Modifier.size(48.dp))
                }
            }
        }
    }
}
