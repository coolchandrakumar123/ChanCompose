package com.chan.compose.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.androidview.WebContext

@Composable
fun renderViews(webParams: String = "https://www.google.com/chandran", webContext: WebContext) {
    if (WebContext.debug) {
        Log.d("WebCompAct", "renderViews")
    }

    val displayedUrl = state { "https://www.google.com" }

    fun updateDisplayedUrl(newValue: String?) {
        if (!newValue.isNullOrBlank() && newValue != displayedUrl.value) {
            displayedUrl.value = newValue
        }
    }

    /*LinearLayout(
        orientation = LinearLayout.VERTICAL,
        layoutWidth = MATCH_PARENT,
        layoutHeight = MATCH_PARENT
    ) {
        LinearLayout(
            orientation = LinearLayout.HORIZONTAL,
            layoutWidth = MATCH_PARENT,
            layoutHeight = WRAP_CONTENT,
            weightSum = 1f
        ) {
            Button(
                layoutWidth = 40.dp,
                layoutHeight = WRAP_CONTENT,
                text = "",
                onClick = {
                    webContext.goBack()
                })
            Button(
                layoutWidth = 40.dp,
                layoutHeight = WRAP_CONTENT,
                text = ") {",
                onClick = {
                    webContext.goForward()
                })
            EditText(
                layoutWidth = 0.dp,
                layoutHeight = WRAP_CONTENT,
                layoutWeight = 1f,
                singleLine = true,
                controlledText = displayedUrl.value,
                onTextChanged = { s: CharSequence?, _, _, _ ->
                    displayedUrl.value = s.toString()
                })
            Button(
                layoutWidth = WRAP_CONTENT,
                layoutHeight = WRAP_CONTENT,
                text = "Go",
                onClick = {
                    if (displayedUrl.value.isNotBlank()) {
                        if (WebContext.debug) {
                            Log.d("WebCompAct", "setting url to " + displayedUrl.value)
                        }
                        webParams.url = displayedUrl.value
                    }
                })
        }*/

        if (WebContext.debug) {
            Log.d("WebCompAct", "webComponent: start")
        }

        /*WebComponent(
            url = webParams,
            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    updateDisplayedUrl(url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    updateDisplayedUrl(url)
                }

                // We support API 21 and above, so we're using the deprecated version.
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    updateDisplayedUrl(url)
                    return false
                }
            },
            webContext = webContext
        )*/

        if (WebContext.debug) {
            Log.d("WebCompAct", "webComponent: end")
        }
    }
