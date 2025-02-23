package org.hinsun.music.extensions

fun Int.toDurationString(): String {
    val seconds = this % 60
    val minutes = this / 60

    return "%02d:%02ds".format(minutes, seconds)
}