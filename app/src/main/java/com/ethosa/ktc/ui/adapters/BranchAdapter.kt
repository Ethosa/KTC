package com.ethosa.ktc.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R
import com.ethosa.ktc.college.timetable.Branches
import com.ethosa.ktc.databinding.LayoutBranchBinding
import com.ethosa.ktc.ui.fragments.TimetableFragment

/**
 * Provides RecyclerView.Adapter behavior for branches
 */
class BranchAdapter(
    private val timetableFragment: TimetableFragment,
    private val items: Branches
) : RecyclerView.Adapter<BranchAdapter.ViewHolder>() {
    /**
     * Provides RecyclerView.ViewHolder behavior
     * Also includes BranchAdapter.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutBranchBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_branch, parent, false)
        )
    }

    /**
     * Builds branches
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val branch = items[position]
        binding.btn.text = branch.title
        binding.btn.setOnClickListener {
            if (Preferences.isStudent)
                timetableFragment.fetchCourses(branch.id)
            else
                timetableFragment.fetchTeacherList(branch.id)
            Preferences.branch = branch
        }
    }

    /**
     * @return items count
     */
    override fun getItemCount(): Int = items.size
}