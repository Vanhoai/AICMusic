package org.hinsun.music.playback

interface Queue {
    val songs: ArrayDeque<Track>
    val hasNextSong: Boolean
}

data class MusicQueue(
    override val songs: ArrayDeque<Track>,
    override val hasNextSong: Boolean,
) : Queue

object EmptyMusicQueue : Queue {
    override val songs: ArrayDeque<Track> = ArrayDeque()
    override val hasNextSong: Boolean = false
}