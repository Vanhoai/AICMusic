package org.hinsun.music.presentation.swipe.setting.widgets

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

fun LazyListScope.buildMotion() {
    item {
        var reduceMotion by remember { mutableStateOf(false) }

        SessionSwitch(
            sessionName = "Motion",
            nameSwitch = "Reduce motion",
            description = "Reduces motion and animations to improve performance and battery life.",
            isActive = reduceMotion,
            onChange = { reduceMotion = it }
        )
    }
}

fun LazyListScope.buildCanvas() {
    item {
        var canvasDownload by remember { mutableStateOf(false) }
        var canvasBottomNav by remember { mutableStateOf(false) }
        var canvasWaveSine by remember { mutableStateOf(false) }

        GroupSessionSwitch(
            sessionName = "Canvas",
            description = "Canvas will be draw with some effects for better experience, but it will also consume more memory and performance.",
            options = listOf(
                SessionSwitch(
                    name = "Download",
                    isActive = canvasDownload,
                    onChange = { canvasDownload = it }
                ),
                SessionSwitch(
                    name = "Bottom navigation",
                    isActive = canvasBottomNav,
                    onChange = { canvasBottomNav = it }
                ),
                SessionSwitch(
                    name = "Wave sine",
                    isActive = canvasWaveSine,
                    onChange = { canvasWaveSine = it }
                )
            )
        )
    }
}