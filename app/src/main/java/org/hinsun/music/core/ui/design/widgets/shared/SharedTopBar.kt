package org.hinsun.music.core.ui.design.widgets.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.core.ui.design.theme.AppTheme

@Composable
fun SharedTopBar(
    name: String,
    onBackPress: () -> Unit,
    actions: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(72.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = name,
            style = AppTheme.typography.normal,
            fontSize = 18.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = null,
            alignment = Alignment.CenterStart,
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackPress() },
        )

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            actions()
        }
    }
}