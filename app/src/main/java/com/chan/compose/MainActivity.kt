package com.chan.compose

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import kotlinx.android.synthetic.main.zplatform_fragment_list.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zplatform_fragment_list)
        findViewById<FrameLayout>(R.id.content)?.setContent {
            Greeting("Chandran Android")
        }
        /*content?.setContent {
            Greeting("Chandran Android")
        }*/
        /*setContent {
            MaterialTheme {
                Greeting("Chandran Android")
            }
        }*/
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