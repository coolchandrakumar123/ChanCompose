package com.chan.compose

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.fragment.app.FragmentManager
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.chan.compose.ui.inflateImageView
import com.chan.compose.ui.itemViewHolder
import kotlinx.android.synthetic.main.zplatform_fragment_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var contentView: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zplatform_fragment_list)
        contentView = findViewById<FrameLayout>(R.id.content)
        /*findViewById<FrameLayout>(R.id.content)?.setContent {
            Greeting("Chandran Android")
        }*/
        /*content?.setContent {
            Greeting("Chandran Android")
        }*/
        /*setContent {
            MaterialTheme {
                Greeting("Chandran Android")
            }
        }*/
        //build(this)
        setCheckUI()
        /*CoroutineScope(Dispatchers.IO).launch {
            getBitmapFromURL("", "846548e5cd1229c865b3181850095458")?.let {
                Log.d("ChanLog", "Success: ");
            }
        }*/
    }

    private fun setCheckUI() {
        contentView.setContent {
            Column {
                Greeting("Chandran Check UI Parsing")
                inflateImageView(this@MainActivity::getImage)
            }
        }
    }

    private fun getImage(onFailed: (String) -> Unit, onSuccess: (Bitmap) -> Unit) {
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

    fun build(context: Context) {
        setUIData(context.readJSONFromAsset("TicketUIResponse.json")?: kotlin.run {
            throw Exception("UI Initialization Failed")
        }).takeIf { it }?.let {

        }?: kotlin.run {
            throw Exception("UI Initialization Failed")
        }
    }

    private fun setUIData(uiDataJSONObject: JSONObject): Boolean {
        val jsonArray = uiDataJSONObject.optJSONArray("data")
        jsonArray?.getJSONObject(0)?.getJSONArray("zplatform_templates")?.getJSONObject(0)?.getJSONArray("zplatform_pattern")?.let {
            contentView.setContent {
                Greeting("Chandran AndroidAfter Parsing")
                itemViewHolder(pattern = it, values = getDataHashMap())
            }
        }
        return true
    }

    private fun getDataHashMap() = HashMap<String, Any?>().also { valueMap ->
        valueMap["id"] = "101"
        valueMap["ticketNumber"] = "#10021"
        valueMap["subject"] = "This is Subject to check the long string and string wrapping. Which icon has to align center vertically. Third Line Too Check"
        valueMap["status"] = "On Hold"
        valueMap["contact"] = "ContactDummy"
        valueMap["createdTime"] = "12 Apr 2020"
        valueMap["priority"] = "High"
        valueMap["assignee"] = "Chandrakumar"
        valueMap["channelIcon"] = "ic_ch_email"
        //valueMap["dueDate"] = dueDate?.takeIf { it.isNotEmpty() }?.let { formatDateTimeToDisplay(it) } ?: "NoDate"
        valueMap["dueDate"] = "12 Apr 2020"
        valueMap["dueDateIcon"] = "ic_zplatform_ticket_due"
    }
}

fun Context.readJSONFromAsset(fileName: String): JSONObject? {
    return try {
        val  inputStream: InputStream = assets.open(fileName)
        JSONObject(inputStream.bufferedReader().use{it.readText()})
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Compose $name!")
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Greeting("PreviewContent")
    }
}