package org.ic.tech.music.design.widgets.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.ic.tech.music.design.theme.AppTheme

@Composable
fun BaseScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .background(AppTheme.colors.backgroundPrimary),
        content = content,
    )
}