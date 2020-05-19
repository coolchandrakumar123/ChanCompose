package com.chan.compose.ui

import android.util.Log
import androidx.animation.*
import androidx.compose.*
import androidx.ui.animation.animatedFloat
import androidx.ui.core.*
import androidx.ui.core.gesture.DragObserver
import androidx.ui.core.gesture.rawDragGestureFilter
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.graphics.Paint
import androidx.ui.layout.Stack
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.PxPosition
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import kotlin.math.sign

@Model
data class ChanItemList(
    val data: ArrayList<ChanItem>,
    var isUpdated: Boolean
)

@Model
data class ChanItem(
    val title: String,
    val position: Int
)

@Composable
fun composeAdapter() {
    val listData = ArrayList<ChanItem>()
    for(i in 1..20) {
        listData.add(ChanItem("Item - $i", i))
    }

    var maxPosition = listData.size
    val chanItemList = ChanItemList(listData, true)
    var isUpdateInProgress = false
    scrollListenAdapterList(chanItemList) { position ->
        if((position + 6) > maxPosition && !isUpdateInProgress) {
            Log.d("ChanLog", "LoadMore: ");
            isUpdateInProgress = true
            val tempData = ArrayList<ChanItem>()
            val startIndex = maxPosition + 1
            val endIndex = startIndex + 20
            for(i in startIndex..endIndex) {
                tempData.add(ChanItem("Item - $i", i))
            }
            chanItemList.data.addAll(tempData)
            chanItemList.isUpdated = true
            maxPosition = chanItemList.data.size
            isUpdateInProgress = false
        }
    }
}

@Composable
fun scrollListenAdapterList(chanItemList: ChanItemList, positionObserver: (Int) -> Unit) {
    val updateStatus = state { chanItemList.isUpdated }
    if(updateStatus.value) {
        AdapterList(data = chanItemList.data) {
            key(v1 = it.position) {
                //adapterItem(it, positionObserver)
                adapterSwipeItem(it, positionObserver)
            }
        }
        chanItemList.isUpdated = false
    }
}

@Composable
fun adapterItem(chanItem: ChanItem, positionObserver: (Int) -> Unit) {
    onActive(callback = {
        positionObserver(chanItem.position)
    })
    Button(
        modifier = Modifier.padding(all = 8.dp),
        enabled = false,
        onClick = {
            //testItem.count += 1
            //click()
        }
    ) {
        Text(text = "${chanItem.title} , position: ${chanItem.position}")
        //positionObserver(chanItem.position)
    }
}

@Composable
fun adapterSwipeItem(chanItem: ChanItem, positionObserver: (Int) -> Unit) {
    onActive(callback = {
        positionObserver(chanItem.position)
    })
    swipeLeftToDismiss(bgChildren = {
        Button(onClick = {
            Log.d("ChanLog", "Clicked: Button-${chanItem.position}");
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
    }, fgChildren = {
        Box(modifier = Modifier.fillMaxWidth()
            .preferredHeight(120.dp),
            gravity = ContentGravity.CenterStart
        ) {
            Text(
                text = "${chanItem.title} , position: ${chanItem.position}",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    })
}

@Composable
private fun swipeLeftToDismiss(fgBackgroundColor: Color = MaterialTheme.colors.primary, bgChildren: @Composable() () -> Unit, fgChildren: @Composable() () -> Unit) {
    val itemLeft = animatedFloat(0f)
    val itemWidth = state { 0f }
    val isFlinging = state { false }
    val modifier = Modifier.rawDragGestureFilter(dragObserver = object : DragObserver {
        override fun onStart(downPosition: PxPosition) {
            val xMid = itemWidth.value/2
            if(downPosition.x.value < xMid) {
                itemLeft.setBounds(0f, xMid)
                if (isFlinging.value && itemLeft.targetValue < 100f) {
                    reset()
                }
            }
        }

        private fun reset() {
            itemLeft.snapTo(0f)
        }

        override fun onDrag(dragDistance: PxPosition): PxPosition {
            val xMid = itemWidth.value/2
            if(dragDistance.x.value < xMid) {
                itemLeft.snapTo(itemLeft.targetValue + dragDistance.x.value)
            }
            return dragDistance
        }

        private fun adjustTarget(velocity: Float): (Float) -> TargetAnimation? {
            return { target: Float ->
                // The velocity is fast enough to fly off screen
                if (target <= 0) {
                    null
                } else {
                    val animation = PhysicsBuilder<Float>(dampingRatio = 0.8f, stiffness = 300f)
                    val projectedTarget = target + sign(velocity) * 0.2f * itemWidth.value
                    val offset = 0.2 * itemWidth.value
                    Log.d("ChanLog", "velocity: $velocity, offset: $offset, projectedTarget: $projectedTarget");
                    if (projectedTarget < offset) {
                        TargetAnimation(0f, animation)
                    } else {
                        TargetAnimation(itemWidth.value/2, animation)
                    }
                }
            }
        }

        override fun onStop(velocity: PxPosition) {
            //Log.d("ChanLog", "onStop: ${velocity.x.value}, ${velocity.y.value}");
            isFlinging.value = true
            itemLeft.fling(velocity.x.value,
                ExponentialDecay(3.0f),
                adjustTarget(velocity.x.value),
                onEnd = { endReason, final, _ ->
                    isFlinging.value = false
                    Log.d("ChanLog", "final: $final");
                    if (endReason != AnimationEndReason.Interrupted && final == 0f) {
                        reset()
                    }
                })
        }
    })

    val paint = remember { Paint() }
    paint.color = Color.Red
    Stack {
        bgChildren()

        Stack(modifier = Modifier.drawLayer(
            translationX = itemLeft.value
        )) {
            Box(
                modifier = modifier
                    .onPositioned { coordinates ->
                        //itemWidth.value = coordinates.size.width.value * 2 / 3f
                        itemWidth.value = coordinates.size.width.value.toFloat()
                        //itemHeight.value = coordinates.size.height.value.toFloat()
                    },
                backgroundColor = fgBackgroundColor,
                children = fgChildren
            )
        }
    }

    //swipeRevealItem(modifier, itemLeft, itemWidth, paint)
}