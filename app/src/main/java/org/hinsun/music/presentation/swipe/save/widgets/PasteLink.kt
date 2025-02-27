package org.hinsun.music.presentation.swipe.save.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.core.ui.design.theme.AppTheme

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun PasteLink(onDownload: (url: String) -> Unit) {
    val clipboardManager = LocalClipboardManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val url =
        remember { mutableStateOf("http://192.168.1.4:8080/static/audios/youtube_Z1D26z9l8y8_audio.mp3") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        OutlinedTextField(
            value = url.value,
            onValueChange = { url.value = it },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,

                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,

                disabledIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,

                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White,

                cursorColor = Color.White,
            ),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_url),
                    contentDescription = null,
                    alignment = Alignment.Center
                )
            },
            placeholder = {
                Text(
                    text = "Paste the link here",
                    color = Color.White.copy(alpha = 0.5f),
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(
                    RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        topStart = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .drawBehind {
                    drawLine(
                        Color.White,
                        Offset(0f, 0f),
                        Offset(0f, size.height),
                        strokeWidth = 4f
                    )
                }
                .clickable {
                    if (url.value.isEmpty()) {
                        val clip = clipboardManager.getText()
                        keyboardController?.hide()
                        url.value = clip?.text ?: ""
                    } else onDownload(url.value)
                },
            contentAlignment = Alignment.Center
        ) {
            val id = if (url.value.isEmpty()) R.drawable.ic_copy else R.drawable.ic_download

            Image(
                painter = painterResource(id = id),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}