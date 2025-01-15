package org.ic.tech.music.presentation.onboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.ic.tech.music.design.theme.AppTheme
import org.ic.tech.music.design.widgets.base.BaseScaffold
import org.ic.tech.music.presentation.graphs.NavRoute
import org.ic.tech.music.presentation.onboard.widgets.DualOrbitingArcs
import org.ic.tech.music.presentation.onboard.widgets.OnboardHeading

@Composable
fun OnBoardView(navHostController: NavHostController) {
    val viewModel = hiltViewModel<OnBoardViewModel>()
    val uiState = viewModel.uiState.collectAsState().value

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val sizeBox = (screenWidth - 40 - 8) / 2

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            OnboardHeading(sizeBox)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DualOrbitingArcs()
            }

            Spacer(modifier = Modifier.weight(1f))

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
                    .clickable {
                        navHostController.navigate(NavRoute.AUTH.path)
                    },
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
