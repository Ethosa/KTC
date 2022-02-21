package com.ethosa.ktc.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.objects.timetable.Branches
import com.ethosa.ktc.databinding.BranchBinding
import com.ethosa.ktc.ui.timetable.TimetableFragment

class BranchAdapter(
    private val timetableFragment: TimetableFragment,
    private val items: Branches
) : RecyclerView.Adapter<BranchAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = BranchBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.branch, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val branch = items[position]
        binding.button.text = branch.title
        binding.button.setOnClickListener {
            timetableFragment.fetchCourses(branch.id)
        }
    }

    override fun getItemCount(): Int = items.size
}