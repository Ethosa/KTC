package com.ethosa.ktc.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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