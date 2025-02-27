package org.hinsun.music.presentation.music.playlist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.core.ui.design.theme.AppTheme
import org.hinsun.music.core.ui.design.widgets.base.BaseImage
import org.hinsun.music.core.ui.design.widgets.base.BaseScaffold
import org.hinsun.music.core.ui.design.widgets.shared.SharedCardSong
import org.hinsun.music.core.ui.design.widgets.shared.SharedTopBar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PlaylistView(
    navHostController: NavHostController,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val scrollState = rememberScrollState()

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SharedTopBar(
                name = "",
                onBackPress = { navHostController.popBackStack() },
                actions = {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_heart),
                            contentDescription = null
                        )
                    }
                }
            )

            Text(
                text = "Thanh xuân của chúng ta",
                style = AppTheme.typography.normal,
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                BaseImage(
                    width = 120,
                    height = 120,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Thời gian không chờ đợi một ai và thành xuân luôn là khoảng thời gian đẹp nhất của mỗi chúng ta ⭐\uFE0F",
                        style = AppTheme.typography.italic,
                        fontSize = 14.sp,
                        color = AppTheme.colors.textPrimary,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp),
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
                        color = AppTheme.colors.textPrimary,
                    )

                    Text(
                        text = "Created at 24-12-2024",
                        style = AppTheme.typography.italic,
                        fontSize = 14.sp,
                        color = AppTheme.colors.textPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .drawWithContent {
                            val radius = size.minDimension / 2
                            drawCircle(
                                color = Color.White.copy(alpha = 0.3f),
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
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 12.dp)
                    .scrollable(scrollState, orientation = Orientation.Vertical)
            ) {
                items(20) {
                    SharedCardSong()
                }
            }
        }
    }
}