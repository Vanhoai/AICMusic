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
import kotlin.random.Random


val images = listOf(
    "https://i.pinimg.com/474x/16/08/ee/1608eeae63db519ff7443d5a8f29abf8.jpg",
    "https://i.pinimg.com/474x/2a/64/c9/2a64c9118651ae8e95bc63ad732ce260.jpg",
    "https://i.pinimg.com/474x/5e/06/79/5e0679f5fbd7bde86b48a1823663fc2d.jpg",
    "https://i.pinimg.com/474x/97/80/20/978020d9777914ca66b958722b7d391c.jpg",
    "https://i.pinimg.com/474x/cd/93/42/cd9342c60c17d8949425194c6e749e2c.jpg",
    "https://i.pinimg.com/474x/34/3c/33/343c33e657976026f734bb9a4392ae23.jpg"
)

@Composable
fun BaseImage(
    shape: Shape = RoundedCornerShape(8.dp),
    height: Int = 60,
    width: Int = 60,
) {
    val index = Random.nextInt(0, images.size)

    AsyncImage(
        model = images[index],
        contentDescription = null,
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .clip(shape),
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.High,
        onLoading = {}
    )
}