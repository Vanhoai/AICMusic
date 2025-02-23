package org.hinsun.music.design.widgets.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.database.aggregates.Playlist
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseImage
import kotlin.math.roundToInt

@Composable
fun SharedPlaylistCard(
    playlist: Playlist,
    onPress: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .width((screenWidth * 0.8).dp)
            .padding(end = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFDFFE7))
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) { onPress() }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Thanh xuân của chúng ta 🌟",
                style = AppTheme.typography.normal,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.colors.backgroundPrimary,
                modifier = Modifier.weight(1f)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color.Black, blendMode = BlendMode.SrcIn)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            BaseImage(
                width = (screenWidth * 0.3).roundToInt(),
                height = (screenWidth * 0.3).roundToInt()
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Thời gian không chờ đợi một ai và thành xuân luôn là khoảng thời gian đẹp nhất của mỗi chúng ta ⭐\uFE0F",
                style = AppTheme.typography.italic,
                fontSize = 14.sp,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "1.54 hours - 10 song",
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.backgroundPrimary,
                )

                Text(
                    text = "Created at 24-12-2024",
                    style = AppTheme.typography.italic,
                    fontSize = 14.sp,
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .drawWithContent {
                        val radius = size.minDimension / 2
                        drawCircle(
                            color = Color.Black,
                            radius = radius,
                            style = Stroke(width = 1.dp.toPx())
                        )

                        drawContent()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                )
            }
        }
    }
}