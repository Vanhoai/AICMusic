package org.hinsun.music.database.aggregates

import androidx.room.Embedded
import org.hinsun.music.database.entities.PlaylistEntity

data class Playlist(
    @Embedded val song: PlaylistEntity,
)