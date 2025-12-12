package org.hinsun.music.design.widgets.base

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

const val defaultImage = "https://i.pinimg.com/736x/92/2b/b7/922bb757183414a56f57f034f050424b.jpg"

@Composable
fun BaseImage(
    modifier: Modifier = Modifier,
    url: String? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    height: Int = 60,
    width: Int = 60,
) {
    AsyncImage(
        model = url ?: defaultImage,
        contentDescription = null,
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
            .clip(shape),
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.High,
        onLoading = {}
    )
}