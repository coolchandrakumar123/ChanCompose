package com.chan.compose.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.layout.preferredSizeIn
import androidx.ui.layout.wrapContentHeight
import androidx.ui.res.vectorResource
import androidx.ui.unit.dp

/**
 * Created by chandra-1765$ on 27/04/20$.
 */
@Composable
fun inflateImageView(getImage: ((String)-> Unit, (Bitmap)-> Unit)-> Unit) {
    var photoImage by state<ImageAsset?> { null }
    onCommit("chandran") {
        getImage({
            Log.d("ChanLog", "Failed: ");
        }, {
            photoImage = it.asImageAsset()
            Log.d("ChanLog", "Success: ");
        })
        onDispose {
            photoImage = null
        }
    }

    val image = photoImage
    if(image != null) {
        Box(gravity = ContentGravity.Center,
            modifier = Modifier.wrapContentHeight()) {
            Image(
                asset = image,
                modifier = Modifier.clip(RoundedCornerShape(30.dp)) + Modifier.preferredSizeIn(maxWidth = 68.dp, maxHeight = 68.dp),
                alignment = Alignment.Center
            )
        }
    } else {
        Box(gravity = ContentGravity.Center,
            modifier = Modifier.wrapContentHeight()) {
            Text(text = "No Photo")
        }
    }
}