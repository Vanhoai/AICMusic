package org.hinsun.music.core.playback

import androidx.media3.common.MediaItem

interface Queue {
    val songs: ArrayDeque<MediaItem>
    val hasNextSong: Boolean
}

data class MusicQueue(
    override val songs: ArrayDeque<MediaItem>,
    override val hasNextSong: Boolean,
) : Queue

object EmptyMusicQueue : Queue {
    override val songs: ArrayDeque<MediaItem> = ArrayDeque()
    override val hasNextSong: Boolean = false
}