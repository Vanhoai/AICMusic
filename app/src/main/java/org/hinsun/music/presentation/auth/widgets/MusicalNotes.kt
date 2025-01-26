package org.hinsun.music.presentation.auth.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import org.hinsun.music.R


@Composable
fun MusicalNotes() {
    val resources = LocalContext.current.resources

    Canvas(modifier = Modifier.fillMaxSize()) {
        val noteDrawable = R.drawable.ic_music_note
        val area = size
        val numberOfNotes = 5

        val notes = mutableListOf<Note>()

        for (i in 0 until numberOfNotes) {
            val x = (0..area.width.toInt()).random().toFloat()
            val y = (0..area.height.toInt()).random().toFloat()

            val duration = (10000..20000).random().toLong()
            val rotation = (0..360).random().toFloat()

            notes.add(Note(x, y, rotation, duration))
        }

        notes.forEach { note ->
            val currentTime = System.currentTimeMillis()
            val xOffset = currentTime % note.duration

            val moveFactor = area.width / (2 * note.duration)

            val x = note.x - xOffset * moveFactor
            val y = note.y

            val image = ResourcesCompat.getDrawable(resources, noteDrawable, null)?.toBitmap()
                ?.asImageBitmap()
            if (image != null) {
                drawImage(
                    image = image,
                    alpha = 0.5f,
                    colorFilter = ColorFilter.tint(Color.White),
                    topLeft = Offset(x, y),
                )
            }
        }
    }
}

data class Note(var x: Float, var y: Float, var rotation: Float, var duration: Long)
