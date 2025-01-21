package org.hinsun.music.presentation.swipe.setting.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.shared.SharedTabSlider

fun LazyListScope.buildAppearanceTheme() {
    item {
        Text(
            text = "Theme",
            style = AppTheme.typography.normal,
            fontSize = 18.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
        )

        SharedTabSlider()

        Text(
            text = "Auto theme switches between light and dark themes depending on your device's display mode.",
            style = AppTheme.typography.italic,
            fontSize = 16.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 20.dp)
        )
    }
}

fun LazyListScope.buildAppearanceBrightAreaBackground() {
    item {
        var isBrightAreaBackgroundOn by remember { mutableStateOf(false) }

        SessionSwitch(
            sessionName = "Bright Area Background",
            nameSwitch = "Bright area",
            description = "Hinsun Music uses a bright area background to enhance the user experience. You can turn it off if you prefer.",
            isActive = isBrightAreaBackgroundOn,
            onChange = { isBrightAreaBackgroundOn = it }
        )
    }
}

fun LazyListScope.buildAccessibility() {
    item {
        var reduceVisualTransparency by remember { mutableStateOf(false) }

        SessionSwitch(
            sessionName = "Accessibility",
            nameSwitch = "Reduce visual transparency",
            description = "Reduces transparency of surfaces and disables blur effects. may also improve ui performance on low performance devices.",
            isActive = reduceVisualTransparency,
            onChange = { reduceVisualTransparency = it }
        )
    }
}