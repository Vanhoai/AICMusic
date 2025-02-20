package org.hinsun.music.presentation.music.mini

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseImage
import org.hinsun.music.presentation.graphs.NavRoute
import org.hinsun.music.presentation.graphs.idBackgroundTransition
import org.hinsun.music.presentation.graphs.idImageTransition
import org.hinsun.music.presentation.graphs.idNameTransition

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MiniPlayer(
    navHostController: NavHostController,
    interactionSource: MutableInteractionSource,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier
) {
    Box(
        modifier = modifier

            .padding(12.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                navHostController.navigate(NavRoute.PLAYER.path)
            }
            .sharedElement(
                state = rememberSharedContentState(key = idBackgroundTransition),
                animatedVisibilityScope = animatedVisibilityScope,
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BaseImage(
                width = 48,
                height = 48,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = idImageTransition),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hẹn em mai sau gặp lại (feat. Lamoon)",
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier
                        .sharedElement(
                            state = rememberSharedContentState(key = idNameTransition),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                )

                Text(
                    text = "5:43s",
                    style = AppTheme.typography.italic,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "DurationTransition"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color.White, blendMode = BlendMode.SrcIn)
            )
        }
    }
}