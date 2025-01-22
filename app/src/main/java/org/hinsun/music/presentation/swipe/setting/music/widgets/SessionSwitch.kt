package org.hinsun.music.presentation.swipe.setting.music.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseSwitch

@Composable
fun SessionSwitch(
    sessionName: String,
    nameSwitch: String,
    description: String,
    isActive: Boolean = false,
    onChange: (Boolean) -> Unit = {}
) {
    Text(
        text = sessionName,
        style = AppTheme.typography.normal,
        fontSize = 18.sp,
        color = AppTheme.colors.textPrimary,
        modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF595959), RoundedCornerShape(8.dp))
            .background(Color(0xFF4A4A4A), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nameSwitch,
                style = AppTheme.typography.normal,
                fontSize = 16.sp,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            BaseSwitch(
                isActive = isActive,
                onChange = { onChange(it) }
            )
        }
    }

    Text(
        text = description,
        style = AppTheme.typography.italic,
        fontSize = 16.sp,
        color = AppTheme.colors.textPrimary,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 20.dp)
    )
}