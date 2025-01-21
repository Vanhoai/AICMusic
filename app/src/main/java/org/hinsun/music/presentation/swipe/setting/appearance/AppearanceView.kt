package org.hinsun.music.presentation.swipe.setting.appearance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.design.widgets.shared.SharedTabSlider
import org.hinsun.music.design.widgets.shared.SharedTopBar

@Composable
fun AppearanceView(navHostController: NavHostController) {

    val scrollState = rememberScrollState()

    BaseScaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            item {
                SharedTopBar(onBackPress = { navHostController.popBackStack() })
            }

            item {
                Text(
                    text = "Theme",
                    style = AppTheme.typography.normal,
                    fontSize = 18.sp,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
                )

                SharedTabSlider()

                Text(
                    text = "Auto theme switches between light and dark themes depending on your device's display mode.",
                    style = AppTheme.typography.italic,
                    fontSize = 16.sp,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}