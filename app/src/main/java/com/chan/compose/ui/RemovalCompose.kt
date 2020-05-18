package com.chan.compose.ui

import androidx.compose.Composable
import androidx.compose.frames.ModelList
import androidx.compose.key
import androidx.compose.*
import androidx.ui.animation.DpToVectorConverter
import androidx.ui.animation.animatedValue
import androidx.ui.core.DensityAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.onPositioned
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.preferredHeight
import androidx.ui.material.Button
import androidx.ui.unit.dp

/**
 * Created by chandra-1765$ on 04/05/20$.
 */
@Composable
fun TestList(list: ModelList<String>) {

    Column {
        for (item in list) {
            println("Recomposed outer outer ($item)")
            RemovalArea(item) {
                Column {
                    println("Recomposed inner ($item)")
                    Spacer(modifier = Modifier.preferredHeight(5.dp))
                    Button(
                        onClick = { collapseArea { list.remove(item) } },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(item)
                    }
                }
            }
        }
    }
}


interface RemovalAreaScope {
    fun collapseArea(onDone: () -> Unit)
}

@Composable
fun RemovalArea(id: Any?, child: @Composable() RemovalAreaScope.() -> Unit) {

    key(id) {
        var startedRemoval by state { false }
        val height = animatedValue(initVal = 0.dp, converter = DpToVectorConverter)
        val scope =
            object : RemovalAreaScope {
                override fun collapseArea(onDone: () -> Unit) {
                    startedRemoval = true
                    height.animateTo(0.dp,
                        onEnd = { _, _ ->
                            onDone()
                        }
                    )
                }
            }

        with(DensityAmbient.current) {
            Box(modifier =
            when {
                !startedRemoval -> Modifier.onPositioned { height.snapTo(it.size.height.toDp()) }
                else -> Modifier.preferredHeight(height.value)
            }
            ) {
                if (!startedRemoval) {
                    scope.child()
                }
            }
        }

    }
}