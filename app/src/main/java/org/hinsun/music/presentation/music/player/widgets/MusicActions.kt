package org.hinsun.music.presentation.music.player.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme


data class MusicOption(
    val name: String,
    @DrawableRes val icon: Int,
)

val musicOptions = listOf(
    MusicOption(
        name = "Share",
        icon = R.drawable.ic_settings
    ),
    MusicOption(
        name = "Save",
        icon = R.drawable.ic_save
    ),
    MusicOption(
        name = "Favorite",
        icon = R.drawable.ic_heart
    ),
)

@Composable
fun MusicActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        musicOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = option.icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = option.name,
                    style = AppTheme.typography.normal,
                    fontSize = 14.sp,
                    color = AppTheme.colors.textPrimary,
                )
            }
        }
    }
}