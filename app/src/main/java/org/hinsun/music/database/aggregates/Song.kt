package org.hinsun.music.database.aggregates

import androidx.room.Embedded
import org.hinsun.music.database.entities.SongEntity

data class Song @JvmOverloads constructor(
    @Embedded val song: SongEntity,
)