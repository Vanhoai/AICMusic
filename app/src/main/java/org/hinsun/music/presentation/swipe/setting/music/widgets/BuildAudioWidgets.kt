package org.hinsun.music.presentation.swipe.setting.music.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.core.ui.design.theme.AppTheme
import org.hinsun.music.core.ui.design.widgets.base.BaseTabSlider
import org.hinsun.music.core.ui.design.widgets.base.TabSliderOption

val audioFormats = listOf(
    TabSliderOption(name = "Best"),
    TabSliderOption(name = "Mp3"),
    TabSliderOption(name = "Ogg"),
    TabSliderOption(name = "Wav"),
    TabSliderOption(name = "Opus"),
)

fun LazyListScope.buildAudioFormat() {
    item {
        Text(
            text = "Audio format",
            style = AppTheme.typography.normal,
            fontSize = 18.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
        )

        BaseTabSlider(options = audioFormats)

        Text(
            text = "All formats but \"best\" are converted from the source format, there will be some quality loss. when \"best\" format is selected, the audio is kept in its original format whenever possible.",
            style = AppTheme.typography.italic,
            fontSize = 16.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 20.dp)
        )
    }
}

val audioBitrate = listOf(
    TabSliderOption(name = "320kb/s"),
    TabSliderOption(name = "256kb/s"),
    TabSliderOption(name = "128kb/s"),
    TabSliderOption(name = "96kb/s"),
)

fun LazyListScope.buildAudioBitrate() {
    item {
        Text(
            text = "Audio bitrate",
            style = AppTheme.typography.normal,
            fontSize = 18.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
        )

        BaseTabSlider(options = audioBitrate)

        Text(
            text = "Bitrate is applied only when converting audio to a lossy format. cobalt can't improve the source audio quality, so choosing a bitrate over 128kbps may inflate the file size with no audible difference. perceived quality may vary by format.",
            style = AppTheme.typography.italic,
            fontSize = 16.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 20.dp)
        )
    }
}