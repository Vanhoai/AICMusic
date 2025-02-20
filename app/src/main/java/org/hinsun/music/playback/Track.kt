package org.hinsun.music.playback

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import org.hinsun.music.R

data class Track(
    val name: String,
    val desc: String,
    @RawRes val id: Int,
    @DrawableRes val image: Int
)

val tracks = listOf(
    Track(
        name = "Smile Flower",
        desc = "Smile Flower",
        id = R.raw.audio,
        image = R.drawable.smile_flower
    ),
    Track(
        name = "Beautiful Crush",
        desc = "Beautiful Crush",
        id = R.raw.audio,
        image = R.drawable.beautiful_crush
    ),
    Track(
        name = "Because I miss you",
        desc = "Because I miss you",
        id = R.raw.audio,
        image = R.drawable.beautiful_crush
    ),
    Track(
        name = "180 Degrees",
        desc = "Because I miss you",
        id = R.raw.audio,
        image = R.drawable.beautiful_crush
    ),
    Track(
        name = "Call of silence",
        desc = "Because I miss you",
        id = R.raw.audio,
        image = R.drawable.beautiful_crush
    ),
    Track(
        name = "One last time",
        desc = "Because I miss you",
        id = R.raw.audio,
        image = R.drawable.beautiful_crush
    )
)