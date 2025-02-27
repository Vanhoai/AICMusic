package org.hinsun.music.core.database.entities

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(
    tableName = "songs",
    indices = []
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    @ColumnInfo(name = "audio_uri") val audioUri: String,
    @ColumnInfo(name = "thumbnail_uri") val thumbnailUri: String,
    val duration: Int,
    @ColumnInfo(name = "date_downloaded") val dateDownloaded: Long,
) {
    companion object {
        fun newSong(
            title: String,
            audioUri: String,
            thumbnailUri: String,
            duration: Int
        ): SongEntity {
            return SongEntity(
                id = 0,
                title = title,
                audioUri = audioUri,
                thumbnailUri = thumbnailUri,
                duration = duration,
                dateDownloaded = 0,
            )
        }
    }
}
