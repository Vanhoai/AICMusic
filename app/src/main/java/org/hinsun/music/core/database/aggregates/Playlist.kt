package org.hinsun.music.core.database.aggregates

import androidx.room.Embedded
import org.hinsun.music.core.database.entities.PlaylistEntity

data class Playlist(
    @Embedded val song: PlaylistEntity,
)