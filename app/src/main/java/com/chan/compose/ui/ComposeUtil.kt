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
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.layout.*
import androidx.ui.layout.RowScope.gravity
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.chan.compose.R

/**
 * Created by chandra-1765$ on 27/04/20$.
 */

@Composable
fun textView(name: String) {
    Text(text = "$name!")
}

@Composable
fun inflateImageView(getImage: (String, (String)-> Unit, (Bitmap)-> Unit)-> Unit) {
    var photoImage by state<ImageAsset?> { null }
    onCommit("chandran") {
        getImage("1234", {
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

@Composable
fun composePhotoNameExperiment(photoName: String, width: Dp = 100.dp, height: Dp = 100.dp) {
    Box(
        modifier = Modifier.preferredSize(width = width, height = height) + Modifier.drawBackground(color = Color.LightGray, shape = CircleShape) + Modifier.None,
        gravity = ContentGravity.Center
    ) {
        Text(
            text = photoName.slice(0..1).orEmpty(),
            modifier = Modifier.gravity(RowAlign.Center),
            style = TextStyle(color = Color.Blue)
        )
    }
}

@Composable
fun composeWeightExperiment() {
    Row(Modifier.padding(16.dp), arrangement = Arrangement.Center) {
        Column(Modifier.weight(1f)) {
            Row {
                Image(vectorResource(R.drawable.ic_ch_email), Modifier.padding(end = 16.dp).preferredSize(18.dp, 16.dp))
                Text(
                    text = "Testing Please Ignore. Need to check second line as well",
                    style = MaterialTheme.typography.body1
                )
            }
            Row {
                Text(
                    text = "#15253",
                    style = MaterialTheme.typography.body2
                )
                Box(
                    modifier = Modifier.preferredSize(width = 4.dp, height = 4.dp).padding(2.dp),
                    backgroundColor = Color.Blue,
                    shape = CircleShape
                )
                Text(
                    text = "Basheer Ahamed",
                    style = MaterialTheme.typography.body2
                )
                Box(
                    modifier = Modifier.preferredSize(width = 4.dp, height = 4.dp).padding(2.dp),
                    backgroundColor = Color.Blue,
                    shape = CircleShape
                )
                Text(
                    text = "Zoho Corp",
                    style = MaterialTheme.typography.body2
                )
            }
            Row {
                Image(vectorResource(R.drawable.ic_zplatform_ticket_due), Modifier.padding(end = 16.dp).preferredSize(12.dp, 12.dp))
                Text(
                    text = "09 Apr 2020 04:02 pm",
                    style = MaterialTheme.typography.body2
                )
                Box(
                    modifier = Modifier.preferredSize(width = 4.dp, height = 4.dp).padding(2.dp),
                    backgroundColor = Color.Blue,
                    shape = CircleShape
                )
                Text(
                    text = "Open",
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Modifier.padding(top = 8.dp, bottom = 8.dp)
        Box(modifier = Modifier.wrapContentSize()) {
            //textView("Chandran Check UI Parsing")
            //textView("Second Line")
            composePhotoNameExperiment("CH", 48.dp, 48.dp)
            composePhotoNameExperiment("KU", 20.dp, 20.dp)
        }
    }
}