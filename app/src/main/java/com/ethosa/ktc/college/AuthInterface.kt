package com.ethosa.ktc.college

import android.webkit.JavascriptInterface
import android.webkit.WebView

/**
 * Provides HTML processing
 */
class AuthInterface {
    var webView: WebView? = null

    /**
     * Processes HTML text
     */
    @JavascriptInterface
    fun processHtml(html: String) { }

    /**
     * Processes cookies
     */
    @JavascriptInterface
    fun getCookies(cookies: String) { }
}