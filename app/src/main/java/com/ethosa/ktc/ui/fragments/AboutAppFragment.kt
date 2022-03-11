package com.ethosa.ktc.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ethosa.ktc.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment() {
    private var _binding: FragmentAboutAppBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)

        binding.authorGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://github.com/ethosa/ktc")
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