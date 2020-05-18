package com.chan.compose.ui

import android.util.Log
import androidx.compose.*
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Text
import androidx.ui.layout.padding
import androidx.ui.material.Button
import androidx.ui.unit.dp

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
                adapterItem(it, positionObserver)
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