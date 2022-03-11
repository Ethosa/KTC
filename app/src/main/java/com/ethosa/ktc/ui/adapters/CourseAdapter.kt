package com.ethosa.ktc.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.timetable.Courses
import com.ethosa.ktc.databinding.LayoutCourseBinding
import com.ethosa.ktc.ui.fragments.TimetableFragment
import com.google.android.material.chip.Chip

/**
 * Provides RecyclerView.Adapter behavior for courses.
 */
class CourseAdapter(
    private val timetableFragment: TimetableFragment,
    private val items: Courses
) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {
    /**
     * Provides RecyclerView.ViewHolder behavior.
     * Also includes CourseBinding.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutCourseBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_course, parent, false)
        )
    }

    /**
     * Builds groups for every course.
     */
    @SuppressLint("SetTextI18n", "NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val item = items[position]
        val back = timetableFragment.resources.getColorStateList(
            R.color.primary, timetableFragment.requireContext().theme)
        val fore = timetableFragment.resources.getColor(
            R.color.btn_text, timetableFragment.requireContext().theme)
        binding.courseTitle.text = "${item.course} курс"
        for (group in item.groups) {
            val chip = Chip(timetableFragment.context)
            chip.text = group.title
            chip.chipBackgroundColor = back
            chip.setTextColor(fore)
            chip.setOnClickListener {
                timetableFragment.fetchTimetable(group.id)
                timetableFragment.group = group
            }
            binding.courseGroup.addView(chip)
        }
    }

    /**
     * @return items count
     */
    override fun getItemCount(): Int = items.size
}