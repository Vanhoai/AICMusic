package org.ic.tech.music.design.widgets.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ic.tech.music.design.theme.AppTheme
import org.ic.tech.music.design.widgets.base.BaseButton

@Composable
fun SharedTextButton(
    text: String,
    modifier: Modifier = Modifier,
) {
    BaseButton(modifier = modifier) {
        Text(
            text = text,
            style = AppTheme.typography.normal,
            fontSize = 16.sp,
            color = AppTheme.colors.textPrimary
        )
    }
}