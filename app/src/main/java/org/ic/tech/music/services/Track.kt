package org.ic.tech.music.services

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import org.ic.tech.music.R

data class Track(
    val name: String,
    val desc: String,
    @RawRes val id: Int,
    @DrawableRes val image: Int
)

val songs = listOf(
    Track(
        name = "Smile flower",
        desc = "Smile flower description",
        R.raw.smile_flower,
        R.drawable.smile_flower,
    ),Track(
        name = "Beautiful Crush",
        desc = "This song for you",
        R.raw.beautiful_crush,
        R.drawable.beautiful_crush,
    ),
)