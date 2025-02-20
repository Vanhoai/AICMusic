package org.hinsun.music.database.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded

@Immutable
data class Song @JvmOverloads constructor(
    @Embedded val song: SongEntity,
)