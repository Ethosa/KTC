package com.ethosa.ktc.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.FragmentSettingsBinding
import com.ethosa.ktc.ui.adapters.SettingsAdapter
import com.ethosa.ktc.utils.SpacingItemDecoration

/**
 * Provides working with KTC last news.
 */
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.settings.layoutManager = LinearLayoutManager(context)
        val itemDecoration = SpacingItemDecoration(0, 32)
        binding.settings.addItemDecoration(itemDecoration)
        binding.settings.adapter = SettingsAdapter(this)

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