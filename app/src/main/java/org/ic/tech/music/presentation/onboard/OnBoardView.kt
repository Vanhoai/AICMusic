package org.ic.tech.music.presentation.onboard

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.ic.tech.music.R
import org.ic.tech.music.design.theme.AppTheme
import org.ic.tech.music.design.widgets.base.BaseScaffold

val images = listOf(
    R.drawable.onboard_3,
    R.drawable.onboard_4,
    R.drawable.onboard_6,
    R.drawable.onboard_7,
)

@Composable
fun OnBoardView(navHostController: NavHostController) {
    val viewModel = hiltViewModel<OnBoardViewModel>()
    val uiState = viewModel.uiState.collectAsState().value

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val sizeBox = (screenWidth - 40 - 8) / 2
    val boxCenter = (screenWidth - 40 - 24) / 4

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backgroundPrimary)
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedImage(height = sizeBox, width = sizeBox, id = R.drawable.onboard_1)
                AnimatedImage(height = sizeBox, width = sizeBox, id = R.drawable.onboard_5)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width(20.dp))
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    images.forEach { image ->
                        AnimatedImage(height = boxCenter, width = boxCenter, id = image)
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                AnimatedImage(
                    width = sizeBox,
                    height = 300,
                    id = R.drawable.onboard_2
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Melody of Life",
                style = AppTheme.typography.italic,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Text(
                text = "Music",
                style = AppTheme.typography.normal,
                fontWeight = FontWeight.Black,
                fontSize = 100.sp,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            val horizontalGradientBrush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFFCD6A),
                    Color(0xFFFF8E52)
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = horizontalGradientBrush)
                    .height(60.dp)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Get Started",
                    style = AppTheme.typography.normal,
                    fontSize = 20.sp,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
fun AnimatedImage(
    width: Int,
    height: Int,
    @DrawableRes id: Int,
) {
    val isPressed = remember { mutableStateOf(false) }

    Image(
        painter = painterResource(id),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .height(height.dp)
            .width(width.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed.value = true
                        tryAwaitRelease()
                        isPressed.value = false
                    }
                )
            }
            .scale(
                animateFloatAsState(
                    targetValue = if (isPressed.value) 0.9f else 1f,
                    animationSpec = tween(300), label = "AnimatedScale"
                ).value
            ),
    )
}