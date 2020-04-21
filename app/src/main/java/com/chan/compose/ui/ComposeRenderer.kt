package com.chan.compose.ui

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.res.vectorResource
import androidx.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by chandra-1765$ on 21/04/20$.
 */


@Composable
fun itemViewHolder(pattern: JSONArray, values: HashMap<String, Any?>) {
    Column(modifier = Modifier.drawBackground(color = Color.White) + Modifier.wrapContentHeight(Alignment.Center), arrangement = Arrangement.Center) {
        for (i in 0 until pattern.length()) {
            //val uiData = pattern.getJSONObject()
            pattern.getJSONObject(i)?.let { component ->
                component.inflateComponent() {
                    values[it]
                }
            }
        }
        //ListDivider()
    }
}

@Composable
private fun JSONObject.inflateComponent(value: (String)-> Any?) {
    when (this.optString("type")) {
        "ColumnSet" -> this.inflateColumnSetView(value)
        "Column" -> this.inflateColumnView(value)
        //is ZPlatformComponentData -> this.inflateComponentView(value)
        else -> {
            this.inflateComponentView(value)
        }
    }
}

@Composable // Orientation - Horizontal
private fun JSONObject.inflateColumnSetView(value: (String)-> Any?) {
    this.optJSONArray("columns")?.let { columns ->
        Row(arrangement = Arrangement.Center, modifier = Modifier.padding(8.dp)) {
            for (i in 0 until columns.length()) {
                columns.getJSONObject(i)?.inflateComponent(value)
            }
        }
    }
}

@Composable // Orientation - Vertical
private fun JSONObject.inflateColumnView(value: (String) -> Any?) {
    this.optJSONArray("items")?.let { items ->
        Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp), arrangement = Arrangement.Bottom) {
            for (i in 0 until items.length()) {
                items.getJSONObject(i)?.inflateComponent(value)
            }
        }
    }
}

/*private fun JSONArray.forEach() {

}*/

@Composable
private fun JSONObject.inflateComponentView(value: (String) -> Any?) {
    val key = this.optString("key")
    when(this.optString("type")) {
        "Icon" -> {
            (value(key) as? String)?.let {
                val resId = ContextAmbient.current.resources.getIdentifier(it, "drawable", ContextAmbient.current.packageName)
                //colorFilter = ColorFilter.tint(Color.Blue),
                Box(gravity = ContentGravity.Center,
                    modifier = Modifier.wrapContentHeight()) {
                    Image(
                        asset = vectorResource(id = resId),
                        modifier = Modifier.preferredSizeIn(maxWidth = 16.dp, maxHeight = 16.dp),
                        alignment = Alignment.Center
                    )
                }

            }
        }
        "Image" -> {
            (value(key) as? String)?.let {
                val resId = ContextAmbient.current.resources.getIdentifier(it, "drawable", ContextAmbient.current.packageName)
                Box(gravity = ContentGravity.Center,
                    modifier = Modifier.wrapContentHeight()) {
                    Image(
                        asset = vectorResource(id = resId),
                        modifier = Modifier.preferredSizeIn(maxWidth = 16.dp, maxHeight = 16.dp),
                        alignment = Alignment.Center
                    )
                }
            }
        }
        else -> {
            val textVal = value(key) as? String ?: "--"
            Text(
                text = textVal,
                modifier = Modifier.wrapContentSize(align = Alignment.CenterStart)
            )
        }
    }
}