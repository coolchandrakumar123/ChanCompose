package com.chan.compose.ui

/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log
import androidx.animation.*
import androidx.compose.*
import androidx.ui.animation.animatedFloat
import androidx.ui.core.*
import androidx.ui.core.gesture.DragObserver
import androidx.ui.core.gesture.rawDragGestureFilter
import androidx.ui.foundation.Box
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.Text
import androidx.ui.foundation.gestures.DragDirection
import androidx.ui.foundation.gestures.draggable
import androidx.ui.geometry.Rect
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.layout.*
import androidx.ui.material.AlertDialog
import androidx.ui.material.Button
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.PxPosition
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import kotlin.math.sign

/**
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-master-dev:ui/ui-animation/integration-tests/animation-demos/src/main/java/androidx/ui/animation/demos/SwipeToDismissDemo.kt
 */

@Composable
fun swipeToDismissDemo() {
    swipeUsingDraggable()
    //swipeLeftToDismiss()
    /*Column(modifier = Modifier.padding(top = 24.dp) ) {
        //SwipeToDismiss()
        Text(
            "Swipe up to dismiss",
            style = TextStyle(fontSize = 30.sp),
            modifier = Modifier.padding(40.dp)
        )
    }*/
}

private val height = 1600f
private val itemHeight = 1600f * 2 / 3f
private val padding = 10f

@Composable
private fun swipeLeftToDismiss() {
    val itemLeft = animatedFloat(0f)
    //val index = state { 0 }
    val itemWidth = state { 0f }
    val isFlinging = state { false }
    val modifier = Modifier.rawDragGestureFilter(dragObserver = object : DragObserver {
        override fun onStart(downPosition: PxPosition) {
            Log.d("ChanLog", "onStart: ${downPosition.x.value}, ${downPosition.y.value}");
            itemLeft.setBounds(0f, itemWidth.value/3)
            /*itemBottom.setBounds(0f, height)
            if (isFlinging.value && itemBottom.targetValue < 100f) {
                reset()
            }*/
        }

        private fun reset() {
            itemLeft.snapTo(0f)
            /*index.value--
            if (index.value < 0) {
                index.value += colors.size
            }*/
        }

        override fun onDrag(dragDistance: PxPosition): PxPosition {
            Log.d("ChanLog", "onDrag: ${dragDistance.x.value}, ${dragDistance.y.value}");
            itemLeft.snapTo(itemLeft.targetValue + dragDistance.x.value)
            return dragDistance
        }

        private fun adjustTarget(velocity: Float): (Float) -> TargetAnimation? {
            return { target: Float ->
                // The velocity is fast enough to fly off screen
                if (target <= 0) {
                    null
                } else {
                    val animation = PhysicsBuilder<Float>(dampingRatio = 0.8f, stiffness = 300f)
                    val projectedTarget = target + sign(velocity) * 0.2f * height
                    if (projectedTarget < 0.6 * height) {
                        TargetAnimation(0f, animation)
                    } else {
                        TargetAnimation(height, animation)
                    }
                }
            }
        }

        override fun onStop(velocity: PxPosition) {
            Log.d("ChanLog", "onStop: ${velocity.x.value}, ${velocity.y.value}");
            //isFlinging.value = true
            /*itemLeft.fling(velocity.x.value,
                ExponentialDecay(3.0f),
                adjustTarget(velocity.x.value),
                onEnd = { endReason, final, _ ->
                    isFlinging.value = false
                    if (endReason != AnimationEndReason.Interrupted && final == 0f) {
                        //reset()
                    }
                })*/
        }
    })

    val paint = remember { Paint() }
    paint.color = Color.Red
    Button(onClick = {
        Log.d("ChanLog", "Clicked: Button");
    }, backgroundColor = Color.LightGray,
        modifier = Modifier.padding(all = 16.dp).drawOpacity(1f)) { //itemLeft.value/300f
        Text(
            text = "Delete",
            style = TextStyle(
                color = Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
    Stack(modifier = Modifier.drawLayer(
        translationX = itemLeft.value
    )) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .preferredHeight(120.dp)
                .onPositioned { coordinates ->
                    //itemWidth.value = coordinates.size.width.value * 2 / 3f
                    itemWidth.value = coordinates.size.width.value.toFloat()
                },
            backgroundColor = Color.Blue,
            gravity = Alignment.Center
        ) {
            Text(
                text = "Swipe Item",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

    //swipeRevealItem(modifier, itemLeft, itemWidth, paint)
}

@Composable
private fun swipeRevealItem(modifier: Modifier, itemLeft: AnimatedFloat, itemWidth: MutableState<Float>, paint: Paint) {
    Canvas(
        modifier.fillMaxWidth()
            .preferredHeight(120.dp)
            .onPositioned { coordinates ->
                itemWidth.value = coordinates.size.width.value.toFloat()
            }
    ) {
        drawRect(
            rect = Rect(
                itemLeft.value,
                0f,
                itemWidth.value,
                120f
            ),
            paint = paint
        )
    }
}

@Composable
fun swipeUsingDraggable() {
    val itemLeft = animatedFloat(0f)
    val itemWidth = state { 0f }
    itemLeft.setBounds(0f, itemWidth.value/3)
    val draggable = Modifier.draggable(
        dragDirection = DragDirection.Horizontal,
        onDragDeltaConsumptionRequested = { delta ->
            Log.d("ChanLog", "onDragDeltaConsumptionRequested: $delta");
            val old = itemLeft.value
            itemLeft.snapTo(itemLeft.value + delta)
            //itemLeft.value - old
            delta
        },
        onDragStopped = {
            Log.d("ChanLog", "onDragStopped: $it");
            //position.fling(flingConfig, it)
        }
    )

    val paint = remember { Paint() }
    paint.color = Color.Red
    Button(
        onClick = {
            Log.d("ChanLog", "Clicked: Button");
        }, backgroundColor = Color.LightGray,
        modifier = Modifier.padding(all = 16.dp)
            .drawOpacity(1f)
            .offset(
                x = 0.dp,
                y = 0.dp
            )
    ) { //itemLeft.value/300f
        Text(
            text = "Delete",
            style = TextStyle(
                color = Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
    Stack(modifier = Modifier.offset(
        x = itemLeft.value.dp,
        y = 0.dp
    )) {
        Box(
            modifier = draggable
                .fillMaxWidth()
                .preferredHeight(120.dp)
                .onPositioned { coordinates ->
                    //itemWidth.value = coordinates.size.width.value * 2 / 3f
                    itemWidth.value = coordinates.size.width.value.toFloat()
                },
            backgroundColor = Color.Blue,
            gravity = Alignment.Center
        ) {
            Button(onClick = {
                Log.d("ChanLog", "Clicked: SwipeItem");
            }) {
                Text(
                    text = "Swipe Item",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun SwipeToDismiss() {
    val itemBottom = animatedFloat(height)
    val index = state { 0 }
    val itemWidth = state { 0f }
    val isFlinging = state { false }
    val modifier = Modifier.rawDragGestureFilter(dragObserver = object : DragObserver {
        override fun onStart(downPosition: PxPosition) {
            itemBottom.setBounds(0f, height)
            if (isFlinging.value && itemBottom.targetValue < 100f) {
                reset()
            }
        }

        private fun reset() {
            itemBottom.snapTo(height)
            index.value--
            if (index.value < 0) {
                index.value += colors.size
            }
        }

        override fun onDrag(dragDistance: PxPosition): PxPosition {
            itemBottom.snapTo(itemBottom.targetValue + dragDistance.y.value)
            return dragDistance
        }

        fun adjustTarget(velocity: Float): (Float) -> TargetAnimation? {
            return { target: Float ->
                // The velocity is fast enough to fly off screen
                if (target <= 0) {
                    null
                } else {
                    val animation = PhysicsBuilder<Float>(dampingRatio = 0.8f, stiffness = 300f)
                    val projectedTarget = target + sign(velocity) * 0.2f * height
                    if (projectedTarget < 0.6 * height) {
                        TargetAnimation(0f, animation)
                    } else {
                        TargetAnimation(height, animation)
                    }
                }
            }
        }

        override fun onStop(velocity: PxPosition) {
            isFlinging.value = true
            itemBottom.fling(velocity.y.value,
                ExponentialDecay(3.0f),
                adjustTarget(velocity.y.value),
                onEnd = { endReason, final, _ ->
                    isFlinging.value = false
                    if (endReason != AnimationEndReason.Interrupted && final == 0f) {
                        reset()
                    }
                })
        }
    })

    val heightDp = with(DensityAmbient.current) { height.toDp() }
    val paint = remember { Paint() }

    Canvas(
        modifier.fillMaxWidth()
            .preferredHeight(heightDp)
            .onPositioned { coordinates ->
                itemWidth.value = coordinates.size.width.value * 2 / 3f
            }
    ) {
        val progress = 1 - itemBottom.value / height
        // TODO: this progress can be used to drive state transitions
        val alpha = 1f - FastOutSlowInEasing(progress)
        val horizontalOffset = progress * itemWidth.value
        drawLeftItems(
            paint, horizontalOffset, itemWidth.value, itemHeight, index.value
        )
        drawDismissingItem(
            paint,
            itemBottom.value, itemWidth.value, itemHeight, index.value + 1,
            alpha
        )
    }
}

private fun DrawScope.drawLeftItems(
    paint: Paint,
    horizontalOffset: Float,
    width: Float,
    height: Float,
    index: Int
) {
    paint.color = colors[index % colors.size]
    paint.alpha = 1f
    val centerX = size.width.value / 2
    val itemRect =
        Rect(
            centerX - width * 1.5f + horizontalOffset + padding,
            size.height.value - height,
            centerX - width * 0.5f + horizontalOffset - padding,
            size.height.value
        )
    drawRect(itemRect, paint)

    if (itemRect.left >= 0) {
        // draw another item
        paint.color = colors[(index - 1 + colors.size) % colors.size]
        drawRect(itemRect.translate(-width, 0f), paint)
    }
}

private fun DrawScope.drawDismissingItem(
    paint: Paint,
    bottom: Float,
    width: Float,
    height: Float,
    index: Int,
    alpha: Float
) {
    paint.color = colors[index % colors.size]
    paint.alpha = alpha
    val centerX = size.width.value / 2
    drawRect(
        Rect(
            centerX - width / 2 + padding,
            bottom - height,
            centerX + width / 2 - padding,
            bottom
        ),
        paint
    )
}

private val colors = listOf(
    Color(0xFFffd7d7),
    Color(0xFFffe9d6),
    Color(0xFFfffbd0),
    Color(0xFFe3ffd9),
    Color(0xFFd0fff8)
)