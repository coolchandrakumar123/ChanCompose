package com.chan.compose.ui

import android.util.Log
import androidx.animation.*
import androidx.compose.*
import androidx.ui.animation.animatedFloat
import androidx.ui.core.*
import androidx.ui.core.gesture.DragObserver
import androidx.ui.core.gesture.rawDragGestureFilter
import androidx.ui.foundation.*
import androidx.ui.foundation.animation.AnchorsFlingConfig
import androidx.ui.foundation.animation.FlingConfig
import androidx.ui.foundation.animation.fling
import androidx.ui.foundation.gestures.DragDirection
import androidx.ui.foundation.gestures.draggable
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.PxPosition
import androidx.ui.unit.dp
import androidx.ui.unit.px
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
fun composeSimpleAdapter() {
    val listData = ArrayList<ChanItem>()
    for (i in 1..50) {
        listData.add(ChanItem("Item - $i", i))

    }

    AdapterList(data = listData) { item ->
        key(inputs = *arrayOf(item.position)) {
            Button(
                modifier = Modifier.padding(all = 8.dp),
                enabled = false,
                onClick = {

                }
            ) {
                Text(text = "${item.title} , position: ${item.position}")
            }
        }
    }
}

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
            key(inputs = *arrayOf(it.position)) {
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
    swipeCompose(
        swipeDirection = SwipeDirection.LEFT,
        bgChildren = {
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

enum class SwipeDirection {
    LEFT, RIGHT
}

@Composable
private fun swipeCompose(swipeDirection: SwipeDirection = SwipeDirection.LEFT, fgBackgroundColor: Color = MaterialTheme.colors.primary, bgChildren: @Composable() () -> Unit, fgChildren: @Composable() () -> Unit) {
    val itemPosition = animatedFloat(0f)
    val itemWidth = state { 0f }
    val isFlinging = state { false }
    val isDragging = state { false }
    val xMid = itemWidth.value/2 * (if(swipeDirection == SwipeDirection.LEFT) 1 else -1)
    val modifier = Modifier.rawDragGestureFilter(
        dragObserver = object : DragObserver {
        override fun onStart(downPosition: PxPosition) {
            Log.d("ChanLog", "onStart x=: ${downPosition.x.value}, y= ${downPosition.y.value} ");
            //isDragging.value =
            if(downPosition.x.value < xMid) {
                if(swipeDirection == SwipeDirection.LEFT) {
                    itemPosition.setBounds(0f, xMid)
                } else {
                    itemPosition.setBounds(xMid, 0f)
                }
                if (isFlinging.value && itemPosition.targetValue < xMid) {
                    reset()
                }
            }
        }

        private fun reset() {
            itemPosition.snapTo(0f)
        }

        override fun onDrag(dragDistance: PxPosition): PxPosition {
            Log.d("ChanLog", "Drag x=: ${dragDistance.x.value}, y= ${dragDistance.y.value} ");
            if(dragDistance.x.value < xMid) {
                itemPosition.snapTo(itemPosition.targetValue + dragDistance.x.value)
            }
            return dragDistance
        }

        private fun adjustTarget(velocity: Float): (Float) -> TargetAnimation? {
            return { target: Float ->
                // The velocity is fast enough to fly off screen
                if(target <= 0) {
                    null
                } else {
                    val targetOffset = if (target <= 0) -target else target
                    val animation = PhysicsBuilder<Float>(dampingRatio = 0.8f, stiffness = 300f)
                    val projectedTarget = targetOffset + sign(velocity) * 0.2f * itemWidth.value
                    val offset = 0.2 * itemWidth.value
                    Log.d("ChanLog", "target: $target, velocity: $velocity, offset: $offset, projectedTarget: $projectedTarget");
                    if (projectedTarget < offset) {
                        TargetAnimation(0f, animation)
                    } else {
                        TargetAnimation(xMid, animation)
                    }
                }
            }
        }

        override fun onStop(velocity: PxPosition) {
            //Log.d("ChanLog", "onStop: ${velocity.x.value}, ${velocity.y.value}");
            isFlinging.value = true
            itemPosition.fling(velocity.x.value,
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

    Stack {
        bgChildren()
        Stack(modifier = Modifier.drawLayer(
            translationX = itemPosition.value
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

fun log(message: String) {
    Log.d("ChanLog", message);
}

@Composable
fun verticalScroller() {
    Column( modifier = Modifier.padding(10.dp)) {
        for (i in 0..50) {
            SwipeArea(onSwiped = {
                log("Swiped $it")
            }, background = {
                Button(modifier = Modifier.padding(10.dp), onClick = { log("Test") }) {
                    //Icon(asset = Icons.Default)
                    Text(text = "Delete")
                }
            }) {
                Box(modifier = Modifier.fillMaxWidth().preferredHeight(100.dp), backgroundColor = if (i % 2 == 0) Color.LightGray else Color.Gray) {
                    Text("Test message $i")
                }
            }
        }
    }
}
@Composable
fun SwipeArea(modifier: Modifier = Modifier, onSwiped: (Boolean) -> Unit, background: @Composable() () -> Unit = { Box(
    Modifier.fillMaxWidth()) }, children: @Composable() () -> Unit = emptyContent()
) {
    val position = animatedFloat(0f)
    var flingConfig: FlingConfig = AnchorsFlingConfig(listOf(0f, 0f))
    var isSwiped by state { false }
    Stack(modifier = modifier.draggable(
        startDragImmediately = position.isRunning,
        dragDirection = DragDirection.Horizontal,
        onDragStopped = {
            position.fling(flingConfig, it)
        },
        onDragDeltaConsumptionRequested = { delta ->
            position.snapTo(position.value + delta)
            delta
        }
    )) {
        val xOffset = with(DensityAmbient.current) { position.value.toDp() }
        Box(modifier = Modifier.gravity(Alignment.CenterEnd).onChildPositioned {
            val width = it.size.width.value.toFloat()
            flingConfig = AnchorsFlingConfig(anchors = listOf(-width,0f), onAnimationEnd = { _,endValue,_ ->
                //log("Fling end $endValue")
                isSwiped = endValue != 0f
                onSwiped(isSwiped)
            })
            position.setBounds(-width, 0f)
        }) {
            background()
        }
        Clickable(onClick = {}, enabled = !isSwiped) {
            Box(
                Modifier.offset(x = xOffset, y = 0.dp).zIndex(1f)
            ) {
                children()
            }
        }
    }
}

@Composable
fun swipeToRefreshCheck() {
    SwipeToRefreshLayout(
        refreshingState = false,
        onRefresh = {

        },
        swipeIcon = {
            Button(onClick = {}) {
                Text(text = "IC")
            }
        }
    ) {
        composeAdapter()
        /*VerticalScroller {
            Column {
                for(i in 1..20) {
                    adapterItem(chanItem = ChanItem("Item - $i", i), positionObserver = {})
                }
            }
        }*/
    }
}

@Composable
fun SwipeToRefreshLayout(
    refreshingState: Boolean,
    onRefresh: () -> Unit,
    swipeIcon: @Composable() () -> Unit,
    content: @Composable() () -> Unit
) {
    val size = with(DensityAmbient.current) { 100.dp.toPx().value }
    //min is below - to hide
    val min = -size
    val max = size * 2
    StateDraggable(
        state = refreshingState,
        onStateChange = { if (it) onRefresh() },
        anchorsToState = listOf(min to false, max to true),
        animationBuilder = TweenBuilder(),
        dragDirection = DragDirection.Vertical,
        minValue = min,
        maxValue = max
    ) { value ->
        val dpOffset = with(DensityAmbient.current) {
            (value.value * 0.5).px.toDp()
        }
        log("dpOffset: $dpOffset")
        Stack {
            content()
            Box(modifier = Modifier.offset(0.dp, dpOffset), gravity = ContentGravity.TopCenter) {
                swipeIcon()
            }
        }
    }
}