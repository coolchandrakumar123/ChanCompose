package com.chan.compose.ui

import android.graphics.Bitmap
import android.util.Log
import android.widget.TextView
import androidx.compose.*
import androidx.lifecycle.MutableLiveData
import androidx.ui.core.*
import androidx.ui.foundation.*
import androidx.ui.foundation.gestures.DragDirection
import androidx.ui.foundation.gestures.ScrollableState
import androidx.ui.foundation.gestures.scrollable
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.input.ImeAction
import androidx.ui.layout.*
import androidx.ui.layout.RowScope.gravity
import androidx.ui.livedata.observeAsState
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.res.vectorResource
import androidx.ui.text.TextRange
import androidx.ui.text.TextStyle
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import androidx.ui.viewinterop.AndroidView
import com.chan.compose.R
import com.chan.compose.components.HintEditText
import com.chan.compose.components.WebComponent
import com.chan.compose.components.WebContext

/**
 * Created by chandra-1765$ on 27/04/20$.
 */

@Composable
fun textView(name: String) {
    Text(text = "$name!")
}


@Composable
fun inflateTestCompose() {
    /*Column(modifier = Modifier.padding(all = 16.dp)) {
        textView("Image Loading Parsing")
        inflateImageViewByLiveData(ImageUtil::getImageByLiveData)
        //inflateImageView(ImageUtil::getImage)
    }*/
    /*Box(modifier = Modifier.wrapContentSize()) {
        //textView("Chandran Check UI Parsing")
        //textView("Second Line")
        composePhotoNameExperiment("CH")
        composePhotoNameExperiment("KU", 40.dp, 40.dp)
    }*/
    //composeWeightExperiment()
    /*MaterialTheme {
        TestList(modelListOf("A", "B", "C", "D"))
    }*/
    //SwipeToDismissDemo()
    /*val listData = ArrayList<String>()
    for(i in 1 until 200) {
        listData.add("Item - $i")
    }
    composeAdapterList(listData)*/

    //adapterListImageTest()
    //inflateClickableList(TestItem("Title", 1))

    /*val listData = ArrayList<TestItem>()
    for(i in 1 until 3) {
        listData.add(TestItem("Item - $i", i))
    }
    inflateClickableList(TestItemList(listData, 1))*/

    //inflateTextInputField()

    inflateAndroidView()

}

@Composable
fun inflateTextInputField() {
    /*Surface(color = Color.LightGray, modifier = Modifier.padding(16.dp), shape = RoundedCornerShape(5.dp)) {
        TextField(
            value = TextFieldValue(),
            modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth(),
            imeAction = ImeAction.Search,
            onValueChange = {

            }
        )
    }*/
    Column {
        val state = state { TextFieldValue("search", TextRange(6,6)) }
        Surface(color = Color.LightGray, modifier = Modifier.padding(16.dp), shape = RoundedCornerShape(5.dp)) {
            TextField(
                value = state.value,
                modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth(),
                imeAction = ImeAction.Search,
                onValueChange = { state.value = it }
            )
        }
        Text("The textfield has this text: "+state.value)

        HintEditText(
            hintText = "Search",
            modifier = Modifier.padding(16.dp) + Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun inflateImageView(getImage: (String, (String)-> Unit, (Bitmap)-> Unit) -> Unit) {
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
        Box(gravity = ContentGravity.Center) {
            Image(
                asset = image,
                modifier = Modifier.clip(RoundedCornerShape(30.dp)) + Modifier.preferredSizeIn(maxWidth = 68.dp, maxHeight = 68.dp),
                alignment = Alignment.Center
            )
        }
    } else {
        Box(gravity = ContentGravity.Center) {
            Text(text = "No Photo")
        }
    }
}

@Composable
fun adapterListImageTest() {
    val data = ArrayList<String>()
    for (i in 1..100) {
        data.add("Title String")
    }

    AdapterList(data = data) {
        Row(modifier = Modifier.padding(all = 16.dp) + Modifier.preferredHeight(72.dp)) {
            textView(name = it)
            Modifier.padding(all = 8.dp)
            inflateImageViewByLiveData(ImageUtil::getImageByLiveData)
            //inflateImageViewByLiveData(ImageUtil::getImageByLiveData)
            //inflateImageViewByLiveData(ImageUtil::getImageByLiveData)
            //inflateImageView(ImageUtil::getImage)
            //inflateImageView(ImageUtil::getImage)
            //inflateImageView(ImageUtil::getImage)
        }
    }
}

@Composable
fun inflateImageViewByLiveData(getImage: (temp: MutableLiveData<Bitmap>, String) -> Unit) {
    //var photoImage by state<ImageAsset?> { null }
    var photoImage: State<Bitmap?>? = null //= getImage("1234").observeAsState()
    val temp = MutableLiveData<Bitmap>()
    photoImage = temp.observeAsState()
    onCommit("chandran") {
        getImage(temp, "1234")
        //photoImage = getImage("1234").observeAsState()
        /*onDispose {
            photoImage = null
        }*/
    }

    //val image = photoImage
    if(photoImage.value != null) {
        Box(gravity = ContentGravity.Center) {
            Image(
                asset = photoImage.value!!.asImageAsset(),
                modifier = Modifier.clip(RoundedCornerShape(30.dp)) + Modifier.preferredSizeIn(maxWidth = 68.dp, maxHeight = 68.dp),
                alignment = Alignment.Center
            )
        }
    } else {
        Box(gravity = ContentGravity.Center) {
            Text(text = "No Photo")
        }
    }
}

@Composable
fun composePhotoNameExperiment(photoName: String, tagModifier: Modifier, width: Dp = 100.dp, height: Dp = 100.dp) {
    Box(
        modifier = tagModifier + Modifier.preferredSize(width = width, height = height)
                + Modifier.drawBackground(color = Color.LightGray, shape = CircleShape)
        + Modifier.drawBorder(border = Border(color = Color.Blue, size = 1.dp), shape = CircleShape),
        gravity = ContentGravity.Center
    ) {
        Text(
            text = photoName.slice(0..1).orEmpty(),
            modifier = Modifier.gravity(Alignment.CenterVertically),
            style = TextStyle(color = Color.Blue)
        )
    }
}

@Composable
fun composeAdapterList(listData: ArrayList<String>) {
    AdapterList(data = listData, modifier = Modifier.scrollable(
        dragDirection = DragDirection.Vertical,
        onScrollStarted = {
            Log.d("ChanLog", "onScrollStarted: $it");
        },
        onScrollStopped = {
            Log.d("ChanLog", "onScrollStopped: $it");
        },
        scrollableState =  ScrollableState(onScrollDeltaConsumptionRequested = {
            0.0f
        })
    )) {
        //minComposeWeightExperiment(title = it)
        composeWeightExperiment()

    }
    /*val scrollerPosition = ScrollerPosition()
    VerticalScroller(scrollerPosition = scrollerPosition) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            listData.forEach { topic ->
                composeWeightExperiment()
            }
        }
    }*/
}



@Composable
fun minComposeWeightExperiment(title: String) {
    Row(Modifier.padding(16.dp), verticalGravity = Alignment.CenterVertically) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(text = title)
            Row(verticalGravity = Alignment.CenterVertically) {
                Image(vectorResource(R.drawable.ic_ch_email), Modifier.padding(end = 16.dp).preferredSize(18.dp, 16.dp))
                Text(
                    text = "Testing Please Ignore. Need to check second line as well",
                    style = MaterialTheme.typography.body1
                )
            }
            Row(verticalGravity = Alignment.CenterVertically) {
                val modifier = Modifier
                modifier.padding(8.dp)
                val paddingMod = modifier + Modifier.padding(8.dp)
                Column(modifier = modifier.padding(8.dp)) {
                    Text(
                        text = "#15253",
                        style = MaterialTheme.typography.body2
                    )
                }
                Box(
                    modifier = Modifier.preferredSize(width = 20.dp, height = 20.dp).padding(all = 8.dp),
                    backgroundColor = Color.Blue,
                    shape = CircleShape
                )
                Text(
                    text = "Basheer Ahamed",
                    style = MaterialTheme.typography.body2
                )
                Column(modifier = modifier.padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier.preferredSize(width = 4.dp, height = 4.dp),
                        backgroundColor = Color.Red,
                        shape = CircleShape
                    )
                }
                Text(
                    text = "Zoho Corp",
                    style = MaterialTheme.typography.body2
                )
            }
            Row(verticalGravity = Alignment.CenterVertically) {
                Image(vectorResource(R.drawable.ic_zplatform_ticket_due), Modifier.padding(end = 16.dp).preferredSize(12.dp, 12.dp))
                Text(
                    text = "09 Apr 2020 04:02 pm",
                    style = MaterialTheme.typography.body2
                )
                Box(
                    modifier = Modifier.preferredSize(width = 4.dp, height = 4.dp),
                    backgroundColor = Color.Blue,
                    shape = CircleShape
                )
                Text(
                    text = "Open",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun composeWeightExperiment() {
    Row(Modifier.padding(16.dp), verticalGravity = Alignment.CenterVertically) {
        //        horizontalGravity = Alignment.CenterHorizontally,
        //        verticalArrangement = Arrangement.Center
        Column(Modifier.weight(1f)) {
            Row(verticalGravity = Alignment.CenterVertically) {
                Image(vectorResource(R.drawable.ic_ch_email), Modifier.padding(end = 16.dp).preferredSize(18.dp, 16.dp))
                Text(
                    text = "Testing Please Ignore. Need to check second line as well",
                    style = MaterialTheme.typography.body1
                )
            }
            Row(verticalGravity = Alignment.CenterVertically) {
                val modifier = Modifier
                modifier.padding(8.dp)
                val paddingMod = modifier + Modifier.padding(8.dp)
                Column(modifier = modifier.padding(8.dp)) {
                    Text(
                        text = "#15253",
                        style = MaterialTheme.typography.body2
                    )
                }
                Box(
                    modifier = Modifier.preferredSize(width = 20.dp, height = 20.dp).padding(all = 8.dp),
                    backgroundColor = Color.Blue,
                    shape = CircleShape
                )
                Text(
                    text = "Basheer Ahamed",
                    style = MaterialTheme.typography.body2
                )
                Column(modifier = modifier.padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier.preferredSize(width = 4.dp, height = 4.dp),
                        backgroundColor = Color.Red,
                        shape = CircleShape
                    )
                }
                Text(
                    text = "Zoho Corp",
                    style = MaterialTheme.typography.body2
                )
            }
            Row(verticalGravity = Alignment.CenterVertically) {
                Image(vectorResource(R.drawable.ic_zplatform_ticket_due), Modifier.padding(end = 16.dp).preferredSize(12.dp, 12.dp))
                Text(
                    text = "09 Apr 2020 04:02 pm",
                    style = MaterialTheme.typography.body2
                )
                Box(
                    modifier = Modifier.preferredSize(width = 4.dp, height = 4.dp),
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
        /*Box(modifier = Modifier.wrapContentSize()) {
            //textView("Chandran Check UI Parsing")
            //textView("Second Line")
            composePhotoNameExperiment("CH", 48.dp, 48.dp)
            composePhotoNameExperiment("KU", 20.dp, 20.dp)
        }*/

        val constraints: (ConstraintSetBuilderScope.() -> ConstraintSetBuilderScope.ConstrainedLayoutReference)? = null
        ConstraintLayout(constraintSet = ConstraintSet{
            tag("test2").apply {
                //centerVertically()
                //centerHorizontally()
                right constrainTo parent.right
                bottom constrainTo parent.bottom
            }
            constraints?.invoke(this)

        }) {
            composePhotoNameExperiment("CH", Modifier.tag("test1"), 48.dp, 48.dp)
            composePhotoNameExperiment("KU", Modifier.tag("test2"), 20.dp, 20.dp)
        }
    }
}

@Model
data class TestItemList (
    val itemList: ArrayList<TestItem>,
    var count: Int
)

@Model
data class TestItem(
    val title: String,
    var count: Int
)


@Composable
fun inflateClickableList(testItemList: TestItemList) {
    VerticalScroller {
        Column {
            testItemList.itemList.forEach {
                inflateClickableList(it) {
                    testItemList.count += 1
                    testItemList.itemList.add(
                        TestItem("Clicked", testItemList.count)
                    )
                }
            }
        }
    }
}

@Composable
fun inflateClickableList(testItem: TestItem, click: () -> Unit) {
    Button(onClick = {
        //testItem.count += 1
        click()
    }) {
        Text(text = "Item-${testItem.title} , count: ${testItem.count}")
    }
}

@Composable
fun inflateAndroidView() {
    Column(modifier = Modifier.fillMaxWidth().wrapContentSize(), horizontalGravity = Alignment.Start) {
        Text(text = "AndroidView", modifier = Modifier.padding(all = 16.dp))

        /*AndroidView(view = TextView(ContextAmbient.current).apply {
            setText("InsideTextView")
        })*/
        AndroidView(resId = R.layout.textview_layout) {

        }
    }

    /*Column(modifier = Modifier.padding(all = 16.dp)) {
        textView("Web ViewTest")
        Surface(Modifier.width(200.dp).height(200.dp)) {

            WebComponent(
                webContext = WebContext(),
                url = "https://www.google.com/"
            )
        }
    }*/
}