package org.hinsun.music.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.hinsun.music.core.database.aggregates.Playlist
import org.hinsun.music.core.database.aggregates.Song
import org.hinsun.music.core.database.entities.SongEntity

@Dao
interface DatabaseDao {
    // region ==================== QUERIES ====================
    @Transaction
    @Query("SELECT * FROM songs")
    fun getAllSongs(): Flow<List<Song>>

    @Transaction
    @Query("SELECT * FROM songs WHERE id = :id")
    fun getSongById(id: Int): Flow<Song?>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<Playlist>>
    // endregion ==================== QUERIES ====================

    // region ==================== MUTATIONS ====================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSong(song: SongEntity): Long
    // endregion ==================== MUTATIONS ====================

    // region ==================== DELETE ====================
    // endregion ==================== DELETE ====================
}