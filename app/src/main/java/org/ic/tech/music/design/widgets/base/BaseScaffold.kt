package org.ic.tech.music.design.widgets.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.ic.tech.music.design.theme.AppTheme

@Composable
fun BaseScaffold(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val brush = Brush.verticalGradient(listOf(Color(0xFFC0FFA3), Color(0xFFFECCFF)))

    Scaffold(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.backgroundPrimary)
            ) {
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp)
                        .offset(x = (-20).dp, y = (-20).dp)
                        .blur(160.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .clip(RoundedCornerShape(100.dp))
                        .background(brush = brush),
                )

                content(it)
            }
        },
    )
}