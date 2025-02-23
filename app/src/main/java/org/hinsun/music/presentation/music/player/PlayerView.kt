package org.hinsun.music.presentation.music.player

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.hinsun.music.R
import org.hinsun.music.constants.CurrentSongIdKey
import org.hinsun.music.database.LocalDatabase
import org.hinsun.music.database.aggregates.Song
import org.hinsun.music.design.theme.AppTheme
import org.hinsun.music.design.widgets.base.BaseImage
import org.hinsun.music.design.widgets.base.BaseScaffold
import org.hinsun.music.extensions.rememberPreference
import org.hinsun.music.playback.EmptyMusicQueue
import org.hinsun.music.playback.LocalPlayerConnection
import org.hinsun.music.playback.MusicQueue
import org.hinsun.music.presentation.graphs.idBackgroundTransition
import org.hinsun.music.presentation.graphs.idImageTransition
import org.hinsun.music.presentation.graphs.idNameTransition
import org.hinsun.music.presentation.music.player.widgets.MusicActions
import org.hinsun.music.presentation.music.player.widgets.MusicPlayerProgressBar
import timber.log.Timber
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PlayerView(
    navHostController: NavHostController,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val database = LocalDatabase.current
    val context = LocalContext.current
    val playerConnection = LocalPlayerConnection.current ?: return

    val infiniteTransition = rememberInfiniteTransition()
    val angle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    var currentSeconds by remember { mutableIntStateOf(0) }
    var currentSong by remember { mutableStateOf<Song?>(null) }
    val currentSongId by rememberPreference(CurrentSongIdKey, -1)

    val songs = database.getAllSongs().collectAsState(initial = emptyList()).value
    val playbackState by playerConnection.playbackState.collectAsState()

    var position by rememberSaveable(playbackState) {
        mutableLongStateOf(playerConnection.exoPlayer.currentPosition)
    }

    var duration by rememberSaveable(playbackState) {
        mutableLongStateOf(playerConnection.exoPlayer.duration)
    }

    LaunchedEffect(playbackState) {
        if (playbackState == STATE_READY) {
            while (isActive) {
                delay(100)
                position = playerConnection.exoPlayer.currentPosition
                duration = playerConnection.exoPlayer.duration

                Timber.tag("MusicPlayer").d("Position: $position")
                Timber.tag("MusicPlayer").d("Duration: $duration")
            }
        }
    }

    LaunchedEffect(currentSongId) {
        if (currentSongId == -1) {
            database.getAllSongs().collect {
                if (it.isNotEmpty()) currentSong = it[0]
            }
        } else {
            database.getSongById(currentSongId).collect {
                currentSong = it
            }
        }
    }

    BaseScaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black.copy(alpha = 0.5f))
                .sharedElement(
                    state = rememberSharedContentState(key = idBackgroundTransition),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_music),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp)
                    .clickable { navHostController.popBackStack() }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .drawWithContent {
                            drawContent()
                            val radius = size.minDimension / 2
                            val center = this.center

                            val gradient = Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFFFF8A8A),
                                    Color(0xFFCD94FF),
                                    Color(0xFFA8FF44),
                                    Color(0xFFFFCA61),
                                    Color(0xFFFF8A8A),
                                ),
                                center = center
                            )

                            rotate(angle.value, center) {
                                drawCircle(
                                    brush = gradient,
                                    radius = radius,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    BaseImage(
                        width = 240,
                        height = 240,
                        shape = CircleShape,
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = idImageTransition),
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Text(
                    text = currentSong?.song?.title ?: "Unknown",
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier
                        .sharedElement(
                            state = rememberSharedContentState(key = idNameTransition),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                )

                MusicActions()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                MusicPlayerProgressBar(
                    currentSeconds = currentSeconds,
                    totalSeconds = 300,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_previous),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_seed_down),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {
                                val medias = songs.map { it.toMediaItem(context) }
                                val queue = MusicQueue(
                                    songs = medias.toCollection(ArrayDeque()),
                                    hasNextSong = false
                                )

                                playerConnection.playQueue(queue)
                            }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ic_seed_up),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_next),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.3f))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UP NEXT",
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "LYRICS",
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "RELATED",
                    style = AppTheme.typography.normal,
                    fontSize = 16.sp,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
