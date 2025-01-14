package org.ic.tech.music.design.theme

import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import timber.log.Timber

object AppTheme {
    private val _isDarkTheme = mutableStateOf(true)
    val isDarkTheme: Boolean
        @Composable
        get() = _isDarkTheme.value

    val colors: ICMusicColors
        @Composable
        @ReadOnlyComposable
        @RequiresPermission.Read
        get() = LocalColors.current

    val spacing: ICMusicSpacing
        @Composable
        @ReadOnlyComposable
        @RequiresPermission.Read
        get() = LocalSpacing.current

    val typography: ICMusicTypography
        @Composable
        @ReadOnlyComposable
        @RequiresPermission.Read
        get() = LocalTypography.current

    fun updateTheme(isDarkTheme: Boolean) {
        _isDarkTheme.value = isDarkTheme
    }
}

@Composable
fun AICMusicTheme(
    isDarkTheme: Boolean = AppTheme.isDarkTheme,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { ICMusicColors() }
    LaunchedEffect(isDarkTheme) {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (isDarkTheme) colorPalette.updateDarkTheme() else colorPalette.updateLightTheme()
            }

            isDarkTheme -> colorPalette.updateDarkTheme()
            else -> colorPalette.updateLightTheme()
        }
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing,
        LocalColors provides colorPalette,
        LocalTypography provides Typography,
    ) {
        ProvideTextStyle(value = Typography.normal, content = content)
    }
}