package com.ethosa.ktc.ui.adapters

import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.LayoutSettingsButtonBinding
import com.ethosa.ktc.ui.fragments.SettingsFragment

class SettingsAdapter (
    private val fragment: SettingsFragment
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    private var titles: TypedArray = fragment.requireContext()
        .resources.obtainTypedArray(R.array.settings_titles)
    private var icons: TypedArray = fragment.requireContext()
        .resources.obtainTypedArray(R.array.settings_icons)
    private var actions: TypedArray = fragment.requireContext()
        .resources.obtainTypedArray(R.array.settings_actions)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutSettingsButtonBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_settings_button, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val icon = icons.getDrawable(position)
        val title = titles.getString(position)
        val action = actions.getResourceId(position, 0)
        binding.chip.chipIcon = icon
        binding.chip.text = title
        println(action)
        if (action != 0)
            binding.chip.setOnClickListener {
                fragment.findNavController().navigate(action)
            }
    }

    override fun getItemCount(): Int = titles.length()
}