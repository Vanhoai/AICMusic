package org.hinsun.music.database.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(
    tableName = "songs",
    indices = []
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val thumbnail: String? = null,
    val duration: Int = -1, // in seconds
    val totalPlayTime: Long = 0, // in milliseconds
)
