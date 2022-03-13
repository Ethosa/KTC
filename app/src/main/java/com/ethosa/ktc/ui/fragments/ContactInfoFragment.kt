package com.ethosa.ktc.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.FragmentContactInfoBinding


/**
 * Provides contact information behavior
 */
class ContactInfoFragment : Fragment() {
    private var _binding: FragmentContactInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactInfoBinding.inflate(inflater, container, false)
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