package com.ethosa.ktc.college

import android.annotation.SuppressLint
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ethosa.ktc.ui.fragments.ProCollegeFragment

/**
 * Provides work with pro college.
 */
@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
class ProCollege(
    private val fragment: ProCollegeFragment,
) {

    companion object {
        private const val LOGIN_PAGE = "https://pro.kansk-tc.ru/login/index.php"

        private val errors = hashMapOf(
            "Your session has timed out. Please log in again." to
                    "Срок Вашей сессии истек. Пожалуйста войдите ещё раз.",
            "Invalid login, please try again." to
                    "Неправильный логин, попробуйте ещё раз.",
            "Invalid login, please try again" to
                    "Неправильный логин, попробуйте ещё раз."
        )
    }

    init {
        fragment.binding.content.addJavascriptInterface(this, "Android")
        with (fragment.binding.content) {
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
        }
    }

    /**
     * Authorization in pro college using WebView.
     */
    @Suppress("DEPRECATION")
    fun auth(username: String, password: String) {
        // Setup WebView ..
        fragment.binding.content.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view?.evaluateJavascript("") {
                    // Auto auth
                    fragment.binding.content.loadUrl("""
                        javascript:
                        
                        let authButton = document.getElementsByClassName("btn-login")[0];
                            authErrors = document.getElementsByClassName("loginerrors mt-3")[0];
                            signOut = document.getElementsByClassName("fa fa-sign-out")[0];
                            signOutButton = document.getElementsByClassName("singlebutton")[0];
                        
                        if (authButton != undefined) {
                            document.getElementsByName("username")[0].value = "$username";
                            document.getElementsByName("password")[0].value = "$password";
                            authButton.click();
                        } else {
                            window.Android.showWebView();
                        }
                        
                        if (authErrors != undefined && authErrors.length != 0) {
                            window.Android.processErrors(
                                authErrors.getElementsByTagName("a")[0].innerHTML
                            );
                        }
                        
                        if (signOut != undefined) {
                            signOut.parentElement.addEventListener("click", function() {
                                window.Android.onSignOut();
                            });
                        }
                        
                        if (signOutButton != undefined) {
                            let btn = signOutButton.getElementsByTagName("button")[0];
                            if (btn.innerHTML == "Выход") {
                                btn.addEventListener("click", function() {
                                    window.Android.onSignOut();
                                });
                            }
                        }
                    """.trimIndent())
                }
            }
        }

        // Page load progress
        fragment.binding.content.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                fragment.binding.contentProgress.progress = newProgress
                if (newProgress >= 100)
                    fragment.binding.contentProgress.visibility = View.GONE
                else
                    fragment.binding.contentProgress.visibility = View.VISIBLE
            }
        }

        fragment.binding.content.loadUrl(LOGIN_PAGE)
    }

    /**
     * Clears WebView and shows errors.
     */
    @JavascriptInterface
    fun processErrors(html: String) {
        onSignOut()
        // Setup error
        if (errors.containsKey(html))
            fragment.binding.password.error = errors[html]
        else
            fragment.binding.password.error = html
    }

    /**
     * Calls on sign out from pro college.
     */
    @JavascriptInterface
    fun onSignOut() {
        fragment.requireActivity().runOnUiThread {
            // Clear WebView
            with (fragment.binding) {
                content.webChromeClient = WebChromeClient()
                content.webViewClient = WebViewClient()
                content.clearHistory()
                content.stopLoading()
                webviewContainer.visibility = View.GONE
                login.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Shows WebView on authorized in account.
     */
    @JavascriptInterface
    fun showWebView() {
        fragment.requireActivity().runOnUiThread {
            fragment.binding.webviewContainer.visibility = View.VISIBLE
            fragment.binding.login.visibility = View.GONE
        }
    }
}