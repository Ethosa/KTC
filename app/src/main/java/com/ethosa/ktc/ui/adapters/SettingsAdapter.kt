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

/**
 * Provides settings behavior.
 */
class SettingsAdapter (
    private val fragment: SettingsFragment
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {
    private val context = fragment.requireContext()
    private val res = context.resources

    private val titles: TypedArray = res.obtainTypedArray(R.array.settings_titles)
    private val icons: TypedArray = res.obtainTypedArray(R.array.settings_icons)
    private val actions: TypedArray = res.obtainTypedArray(R.array.settings_actions)

    /**
     * Provides RecyclerView.ViewHolder behavior
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutSettingsButtonBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_settings_button, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val action = actions.getResourceId(position, 0)
        binding.chip.chipIcon = icons.getDrawable(position)
        binding.chip.text = titles.getString(position)
        if (action != 0)
            binding.chip.setOnClickListener {
                fragment.findNavController().navigate(action)
            }
    }

    override fun getItemCount(): Int = titles.length()
}