package com.ethosa.ktc.college

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

/**
 * Provides work with pro college.
 */
class ProCollege(
    private val wb: WebView
) {
    private val authInterface = AuthInterface()

    companion object {
        private const val LOGIN_PAGE = "https://pro.kansk-tc.ru/login/index.php"
    }

    /**
     * Authorization in pro college using WebView.
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun auth(username: String, password: String) {
        authInterface.webView = wb
        // Setup WebView ..
        wb.settings.javaScriptEnabled = true
        wb.settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
        wb.addJavascriptInterface(authInterface, "Android")
        wb.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view?.evaluateJavascript("") {
                    wb.loadUrl("""
                        javascript:
                        document.getElementsByName("username")[0].value = "$username";
                        document.getElementsByName("password")[0].value = "$password";
                        document.getElementsByClassName("btn-login")[0].click();
                        window.Android.processHtml('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');
                        window.Android.sendCookies(document.cookie);
                    """.trimIndent())
                }
            }
        }

        wb.loadUrl(LOGIN_PAGE)
    }
}