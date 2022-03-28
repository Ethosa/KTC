package com.ethosa.ktc.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import androidx.webkit.WebViewFeature
import com.ethosa.ktc.college.ProCollege
import com.ethosa.ktc.databinding.FragmentProCollegeBinding


/**
 * Fragment which provides working with ProCollege.
 */
class ProCollegeFragment : Fragment() {
    private var _binding: FragmentProCollegeBinding? = null
    private val binding get() = _binding!!
    private lateinit var proCollege: ProCollege
    private lateinit var preferences: SharedPreferences

    companion object {
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProCollegeBinding.inflate(inflater, container, false)

        proCollege = ProCollege(binding.content)
        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        binding.username.editText?.setText(preferences.getString(USERNAME, ""))
        binding.password.editText?.setText(preferences.getString(PASSWORD, ""))

        // Auto dark mode ...
        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    WebSettingsCompat.setForceDark(binding.content.settings, FORCE_DARK_ON)
                }
                Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    WebSettingsCompat.setForceDark(binding.content.settings, FORCE_DARK_OFF)
                }
            }
        }

        // Page load progress
        binding.content.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.contentProgress.progress = newProgress
                if (newProgress >= 100)
                    binding.contentProgress.visibility = View.GONE
                else
                    binding.contentProgress.visibility = View.VISIBLE
            }
        }

        binding.auth.setOnClickListener {
            // Auth in ProCollege
            binding.login.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
            preferences.edit().putString(USERNAME, binding.username.editText?.text.toString()).apply()
            preferences.edit().putString(PASSWORD, binding.password.editText?.text.toString()).apply()
            proCollege.auth(
                binding.username.editText?.text.toString(),
                binding.password.editText?.text.toString()
            )
        }

        return binding.root
    }
}