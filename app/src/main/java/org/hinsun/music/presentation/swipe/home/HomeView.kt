package org.hinsun.music.presentation.swipe.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.hinsun.music.R
import org.hinsun.music.database.LocalDatabase
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.shared.SharedCardSong
import org.hinsun.music.design.widgets.shared.SharedGradientOutlineImage
import org.hinsun.music.design.widgets.shared.SharedPlaylistCard
import org.hinsun.music.design.widgets.shared.SharedRowText
import org.hinsun.music.presentation.graphs.NavRoute
import timber.log.Timber

@Composable
fun HomeView(navHostController: NavHostController) {
    val scrollState = rememberScrollState()
    val database = LocalDatabase.current

    val songs = database.getAllSongs().collectAsState(initial = emptyList())
    Timber.tag("HomeView").d("Songs: $songs")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(scrollState, orientation = Orientation.Vertical)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                )

                SharedGradientOutlineImage()
            }
        }

        item {
            Text(
                text = "Recently",
                style = AppTheme.typography.normal,
                fontSize = 18.sp,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
            )

            for (i in 0..4) {
                SharedCardSong(
                    onPress = {
                        navHostController.navigate(NavRoute.PLAYER.path)
                    }
                )
            }
        }

        item {
            SharedRowText(text = "Playlists")

            LazyRow {
                item { Spacer(modifier = Modifier.width(20.dp)) }
                items(5) {
                    SharedPlaylistCard(onPress = {
                        navHostController.navigate(NavRoute.PLAYLIST.path)
                    })
                }
                item { Spacer(modifier = Modifier.width(8.dp)) }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            SharedRowText(text = "Songs")

            val categories = listOf("Pop", "Rock", "Hip Hop", "Country", "Jazz", "Classical")

            LazyRow {
                item { Spacer(modifier = Modifier.width(20.dp)) }
                items(categories) {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(AppTheme.colors.textPrimary.copy(alpha = 0.3f))
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it,
                            style = AppTheme.typography.normal,
                            fontSize = 16.sp,
                            color = AppTheme.colors.textPrimary,
                        )
                    }
                }

                item { Spacer(modifier = Modifier.width(8.dp)) }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Column {
                for (i in 0..10) {
                    SharedCardSong()
                }
            }
        }
    }
}