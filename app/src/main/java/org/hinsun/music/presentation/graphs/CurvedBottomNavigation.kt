package org.hinsun.music.presentation.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun CurvedBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String,
    onPress: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White.copy(alpha = 0.3f))
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = screenWidth // Độ dài cung
            val d = 80f  // Chiều cao cung

            // Tính bán kính R
            val R = (d / 2f) + (c * c) / (8f * d)

            // Góc quét (angle) bằng radian
            val angle = (c / R) * (180 / Math.PI).toFloat() // Chuyển radian sang độ

            // Tọa độ trung tâm
            val centerX = size.width / 2f
            val centerY = size.height - d // Đỉnh cung nằm ở độ cao d

            // Vẽ cung tròn
            drawArc(
                color = Color.DarkGray,
                startAngle = 90f + angle / 2, // Bắt đầu từ bên trái
                sweepAngle = -angle,         // Quét từ trái sang phải
                useCenter = false,
                topLeft = Offset(centerX - R, centerY - R),
                size = Size(2 * R, 2 * R),
                style = Stroke(width = 5f) // Độ dày của cung tròn
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = null
                    )

                    Text(
                        text = "Home",
                        style = AppTheme.typography.normal,
                    )
                }
            }
        }
    }
}