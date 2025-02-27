package org.hinsun.music.presentation.swipe.setting.music.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import org.hinsun.music.core.ui.design.theme.AppTheme
import org.hinsun.music.core.ui.design.widgets.base.BaseSwitch

data class SessionSwitch(
    val name: String,
    val isActive: Boolean = false,
    val onChange: (Boolean) -> Unit = {}
)

@Composable
fun GroupSessionSwitch(
    sessionName: String,
    description: String,
    options: List<SessionSwitch>,
) {
    Text(
        text = sessionName,
        style = AppTheme.typography.normal,
        fontSize = 18.sp,
        color = AppTheme.colors.textPrimary,
        modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF595959), RoundedCornerShape(8.dp))
                    .background(Color(0xFF4A4A4A), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.name,
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                BaseSwitch(
                    isActive = option.isActive,
                    onChange = { option.onChange(it) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    Text(
        text = description,
        style = AppTheme.typography.italic,
        fontSize = 16.sp,
        color = AppTheme.colors.textPrimary,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
    )
}