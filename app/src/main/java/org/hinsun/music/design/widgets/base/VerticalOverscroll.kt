package org.hinsun.music.design.widgets.base

import android.health.connect.datatypes.units.Velocity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
class VerticalOverscroll(val scope: CoroutineScope, val resistance: Float = 0.2f) :
    OverscrollEffect {
    // Here I use a state because for my app I use the overscroll value to
    // further customize other elements in the list, but you can remove that
    // and have a normal `var ... = Animatable(0f)` if you don't need it
    private var overscrollY by mutableStateOf(Animatable(0f))

    // Ignore small deltas (< 0.5) as this usually comes from floating
    // point rounding issues and can cause scrolling to lock up
    private fun Float.isDeltaValid(): Boolean = abs(this) > 0.5

    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset {
        val deltaY = delta.y

        val sameDirection = sign(deltaY) == sign(overscrollY.value)

        val undoOverscrollDelta =
        // When scrolling the opposite direction of the overscroll
            // subtract from the overscroll first before scrolling
            if (overscrollY.value.isDeltaValid() && !sameDirection) {
                val oldOverscrollY = overscrollY.value
                val newOverscrollY = overscrollY.value + deltaY

                // If all the overscroll is done and we're now scrolling, clamp the overscroll
                // and return the remaining delta
                if (sign(oldOverscrollY) != sign(newOverscrollY)) {
                    scope.launch { overscrollY.snapTo(0f) }
                    deltaY + oldOverscrollY
                }
                // If there is still overscroll subtract from it and return the full
                // delta so that no scrolling occurs
                else {
                    scope.launch { overscrollY.snapTo(overscrollY.value + deltaY * resistance) }
                    deltaY
                }
            } else {
                0f
            }

        val adjustedDelta = deltaY - undoOverscrollDelta
        val scrolledDelta = performScroll(Offset(0f, adjustedDelta)).y
        val overscrollDelta = adjustedDelta - scrolledDelta

        if (overscrollDelta.isDeltaValid() && source == NestedScrollSource.Drag) {
            scope.launch { overscrollY.snapTo(overscrollY.value + overscrollDelta * resistance) }
        }

        return Offset(0f, undoOverscrollDelta + scrolledDelta)
    }

    override suspend fun applyToFling(
        velocity: androidx.compose.ui.unit.Velocity,
        performFling: suspend (androidx.compose.ui.unit.Velocity) -> androidx.compose.ui.unit.Velocity
    ) {
        val consumed = performFling(velocity)
        val remaining = velocity - consumed

        overscrollY.animateTo(
            targetValue = 0f,
            initialVelocity = remaining.y,
            animationSpec = tween(durationMillis = 500, easing = EaseOutQuad)
        )
    }

    override val isInProgress: Boolean
        get() = overscrollY.value != 0f

    override val effectModifier: Modifier =
        Modifier.offset { IntOffset(x = 0, y = overscrollY.value.roundToInt()) }
}