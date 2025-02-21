package org.hinsun.music.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.hinsun.music.database.aggregates.Song
import org.hinsun.music.database.entities.SongEntity

@Dao
interface DatabaseDao {
    // region ==================== QUERIES ====================
    @Transaction
    @Query("SELECT * FROM songs")
    fun getAllSongs(): Flow<List<Song>>
    // endregion ==================== QUERIES ====================

    // region ==================== MUTATIONS ====================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSong(song: SongEntity): Long
    // endregion ==================== MUTATIONS ====================

    // region ==================== DELETE ====================
    // endregion ==================== DELETE ====================
}