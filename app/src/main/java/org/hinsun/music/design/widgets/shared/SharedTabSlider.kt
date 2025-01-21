package org.hinsun.music.design.widgets.shared

import androidx.compose.runtime.Composable
import org.hinsun.music.design.widgets.base.BaseTabSlider
import org.hinsun.music.design.widgets.base.TabSliderOption


@Composable
fun SharedTabSlider() {
    BaseTabSlider(
        options = listOf(
            TabSliderOption(name = "Auto"),
            TabSliderOption(name = "Dark"),
            TabSliderOption(name = "Light"),
        )
    )
}