package com.chan.compose.components

import android.print.PrintDocumentAdapter
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.state
import androidx.ui.core.ContextAmbient
import androidx.ui.foundation.Text
import androidx.ui.material.Button
import androidx.ui.viewinterop.AndroidView

class WebContext {
    companion object {
        val debug = true
    }

    fun createPrintDocumentAdapter(documentName: String): PrintDocumentAdapter {
        validateWebView()
        return webView!!.createPrintDocumentAdapter(documentName)
    }

    fun goForward() {
        validateWebView()
        webView!!.goForward()
    }

    fun goBack() {
        validateWebView()
        webView!!.goBack()
    }

    fun canGoBack(): Boolean {
        validateWebView()
        return webView!!.canGoBack()
    }

    private fun validateWebView() {
        if (webView == null) {
            throw IllegalStateException("The WebView is not initialized yet.")
        }
    }

    internal var webView: WebView? = null
}

private fun WebView.setRef(ref: (WebView) -> Unit) {
    ref(this)
}

private fun WebView.setUrl(url: String) {
    if (originalUrl != url) {
        if (WebContext.debug) {
            Log.d("WebComponent", "WebComponent load url")
        }
        loadUrl(url)
    }
}

@Model
class ClickItem(
    var isClicked: Boolean = false
)

@Composable
fun WebComponent(
    url: String,
    webViewClient: WebViewClient = WebViewClient(),
    webContext: WebContext,
    clickItem: ClickItem = ClickItem()
) {
    if (WebContext.debug) {
        Log.d("WebComponent", "WebComponent compose " + url)
    }
    //Text(text = url)
    /*WebView(
        ref = { webContext.webView = it },
        url = url,
        webViewClient = webViewClient
    )*/
    /*AndroidView(view = WebView(ContextAmbient.current).apply {
        setUrl("chandran-webview")
    })*/

    val isClicked = state{clickItem.isClicked}
    Log.d("ChanLog", "clicked: ${clickItem.isClicked}")

    if (clickItem.isClicked) {
        Log.d("ChanLog", "webview: ")
        /*AndroidView(resId = R.layout.webview_layout) {
            //it.findViewById<WebView>(R.id.webView)?.loadUrl("chandran-test")
        }*/
    } else {
        Log.d("ChanLog", "Button: ")
        Button(onClick = {
            clickItem.isClicked = true
        }) {
            Text(text = "Click for WebView")
        }
    }

}