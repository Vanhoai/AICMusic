package org.hinsun.music.core.database

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import org.hinsun.music.core.database.entities.PlaylistEntity
import org.hinsun.music.core.database.entities.PlaylistSongMap
import org.hinsun.music.core.database.entities.SongEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class MusicDatabase(
    private val delegate: InternalDatabase,
) : DatabaseDao by delegate.dao {
    val openHelper: SupportSQLiteOpenHelper
        get() = delegate.openHelper

    fun query(block: MusicDatabase.() -> Unit) = with(delegate) {
        queryExecutor.execute {
            block(this@MusicDatabase)
        }
    }

    fun transaction(block: MusicDatabase.() -> Unit) = with(delegate) {
        transactionExecutor.execute {
            runInTransaction {
                block(this@MusicDatabase)
            }
        }
    }

    fun close() = delegate.close()
}

@RequiresApi(Build.VERSION_CODES.O)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? =
        if (value != null) LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)
        else null

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? =
        date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
}


@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongMap::class
    ],
    exportSchema = true,
    version = 1
)
@TypeConverters(Converters::class)
abstract class InternalDatabase : RoomDatabase() {
    abstract val dao: DatabaseDao

    companion object {
        private const val DB_NAME = "hinsun.database"

        fun newInstance(context: Context): MusicDatabase =
            MusicDatabase(
                delegate = Room
                    .databaseBuilder(context, InternalDatabase::class.java, DB_NAME)
                    .build()
            )
    }
}

val LocalDatabase = staticCompositionLocalOf<MusicDatabase> { error("No database provided") }