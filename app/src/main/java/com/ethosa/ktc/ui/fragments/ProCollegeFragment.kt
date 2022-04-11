package com.ethosa.ktc.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import androidx.webkit.WebViewFeature
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.college.ProCollege
import com.ethosa.ktc.databinding.FragmentProCollegeBinding


/**
 * Fragment which provides working with ProCollege.
 */
class ProCollegeFragment : Fragment() {
    private var _binding: FragmentProCollegeBinding? = null
    val binding get() = _binding!!

    private lateinit var proCollege: ProCollege
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProCollegeBinding.inflate(inflater, container, false)

        proCollege = ProCollege(this)
        preferences = Preferences(requireContext())
        preferences.load()

        binding.username.editText?.setText(Preferences.proCollegeUsername)
        binding.password.editText?.setText(Preferences.proCollegePassword)

        // Auto dark mode ...
        // Require API Q+
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

        binding.auth.setOnClickListener { auth() }

        return binding.root
    }

    /**
     * Authorizes in the ProCollege
     */
    private fun auth() {
        // Load data from input
        val username = binding.username.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        // check validation
        if (username != "" && password != "") {
            // Save username and password and go to the pro college.
            Preferences.proCollegeUsername = username
            Preferences.proCollegePassword = password
            preferences.saveProCollege()
            proCollege.auth(Preferences.proCollegeUsername, Preferences.proCollegePassword)
            binding.login.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
        } else {
            // Show errors
            if (username == "")
                binding.username.error = "Введите логин"
            if (password == "")
                binding.password.error = "Введите пароль"
        }
    }
}