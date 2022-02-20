package com.ethosa.ktc.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.objects.timetable.Courses
import com.ethosa.ktc.databinding.CourseBinding
import com.ethosa.ktc.ui.timetable.TimetableFragment
import com.google.android.material.chip.Chip

class CourseAdapter(
    private val timetableFragment: TimetableFragment,
    private val items: Courses
) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = CourseBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.course, parent, false)
        return ViewHolder(inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = items[position]
        binding.courseTitle.text = "${item.course} курс"
        for (group in item.groups) {
            val chip = Chip(timetableFragment.context)
            chip.text = group.title
            chip.setOnClickListener {
                timetableFragment.fetchTimetable(group.id)
            }
            binding.courseGroup.addView(chip)
        }
    }

    override fun getItemCount(): Int = items.size
}