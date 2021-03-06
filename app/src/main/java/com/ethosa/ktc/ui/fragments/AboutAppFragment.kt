package com.ethosa.ktc.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ethosa.ktc.Constants
import com.ethosa.ktc.databinding.FragmentAboutAppBinding
import com.ethosa.ktc.ui.dialog.AppUpdater

/**
 * Provides About app fragment behavior.
 */
class AboutAppFragment : Fragment() {
    private var _binding: FragmentAboutAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)

        binding.appVersion.text = AppUpdater.version
        binding.authorGithub.setOnClickListener {
            // Go to Github repo
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.GITHUB_REPO_URL)
            startActivity(intent)
        }

        return binding.root
    }

    /**
     * destroy bindings.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}