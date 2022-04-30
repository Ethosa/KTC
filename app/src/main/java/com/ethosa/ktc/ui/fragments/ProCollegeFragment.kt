package com.ethosa.ktc.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.FORCE_DARK_OFF
import androidx.webkit.WebSettingsCompat.FORCE_DARK_ON
import androidx.webkit.WebViewFeature
import com.ethosa.ktc.Constants
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
    var uploadMessage: ValueCallback<Array<Uri>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProCollegeBinding.inflate(inflater, container, false)

        proCollege = ProCollege(this)
        preferences = Preferences(requireContext())
        preferences.load()

        checkDownloadPermission()

        binding.username.editText?.setText(Preferences.proCollegeUsername)
        binding.password.editText?.setText(Preferences.proCollegePassword)

        if (Preferences.proCollegeUsername != "" && Preferences.proCollegePassword != "")
            auth()

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == Constants.FILECHOOSER_RESULTCODE) {
            if (null == uploadMessage) return
            val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
            uploadMessage?.onReceiveValue(arrayOf(result!!))
            uploadMessage = null
        }
    }

    private fun checkDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                requireActivity(),
                "Разрешение на работу с файлами нужно для сохранения файлов. " +
                        "Разрешите работу с файлами в настройках приложения",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
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
        } else {
            // Show errors
            if (username == "")
                binding.username.error = "Введите логин"
            if (password == "")
                binding.password.error = "Введите пароль"
        }
    }
}