package com.chan.compose.ui

import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.Composable
import androidx.compose.escapeCompose
import androidx.ui.core.Alignment
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.wrapContentSize
import androidx.ui.unit.dp
import androidx.ui.viewinterop.AndroidView
import com.chan.compose.R

@Composable
fun inflateAndroidView() {
    Column(modifier = Modifier.fillMaxWidth().wrapContentSize(), horizontalGravity = Alignment.Start) {
        Text(text = "AndroidView-Compose", modifier = Modifier.padding(all = 16.dp))
        //TextView(text = "Hello World")
        AndroidView(resId = R.layout.textview_layout)
    }
}

@Composable
fun TextView(text: String) {
    escapeCompose {
        val temp = TextView(ContextAmbient.current).apply {
            this.text = "$text - InsideTextView"
        }
        AndroidView(view = temp)
    }
}