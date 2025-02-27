package org.hinsun.music.core.database.aggregates

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.room.Embedded
import org.hinsun.music.core.database.entities.SongEntity
import java.io.File

data class Song(
    @Embedded val song: SongEntity,
) {
    private fun getFile(context: Context, fileName: String): File {
        val dict = File(context.filesDir, "musics")
        if (!dict.exists()) dict.mkdirs()

        val file = File(dict, fileName)
        return file
    }

    // /data/user/0/org.hinsun.music/files/musics/youtube_lXx-kdlxL48_audio.mp3
    fun toMediaItem(context: Context): MediaItem {
        val nameFile = song.audioUri.substringAfterLast("/")

        val file = getFile(context, nameFile)
        val uri = Uri.fromFile(file)
        return MediaItem.fromUri(uri)
    }

    companion object {
        fun default(): Song {
            return Song(
                song = SongEntity.newSong(
                    title = "Hẹn em mai sau gặp lại (feat. Lamoon)",
                    audioUri = "http://10.0.1.66:8080/static/audios/youtube_lXx-kdlxL48_audio.mp3",
                    thumbnailUri = "http://10.0.1.66:8080/static/audios/youtube_lXx-kdlxL48_audio.mp3",
                    duration = 24
                )
            )
        }
    }
}