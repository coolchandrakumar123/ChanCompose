package com.chan.compose.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by chandra-1765$ on 08/05/20$.
 */

internal object ImageUtil {

    internal fun getImage(recordId: String, onFailed: (String) -> Unit, onSuccess: (Bitmap) -> Unit) {
        Log.d("ChanLog", "recordId: $recordId");
        CoroutineScope(Dispatchers.IO).launch {
            getBitmapFromURL("846548e5cd1229c865b3181850095458")?.let {
                Log.d("ChanLog", "Success: ");
                CoroutineScope(Dispatchers.Main).launch {
                    onSuccess(it)
                }
            }?: kotlin.run {
                CoroutineScope(Dispatchers.Main).launch {
                    onFailed("Failed")
                }
            }
        }
    }

    internal fun getImageByLiveData(resultImage: MutableLiveData<Bitmap>, recordId: String) {
        Log.d("ChanLog", "recordId: $recordId");
        CoroutineScope(Dispatchers.IO).launch {
            getBitmapFromURL("846548e5cd1229c865b3181850095458")?.let {
                Log.d("ChanLog", "Success: ");
                CoroutineScope(Dispatchers.Main).launch {
                    //onSuccess(it)
                    resultImage.postValue(it)
                }
            }?: kotlin.run {
                CoroutineScope(Dispatchers.Main).launch {
                    //onFailed("Failed")
                }
            }
        }
    }
}

private fun getBitmapFromURL(oAuthToken: String): Bitmap? {
    return try {
        val url = URL("https://desk.zoho.com/api/v1/agents/196608000009628161/photo?orgId=648638721")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"//NO I18N
        urlConnection.setRequestProperty("Authorization", "Zoho-authtoken $oAuthToken")
        urlConnection.connect()
        Log.d("ChanLog", "responseCode: ${urlConnection.responseCode}");
        val inputStream = BufferedInputStream(urlConnection.inputStream)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}