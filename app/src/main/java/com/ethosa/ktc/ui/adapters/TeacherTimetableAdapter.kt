package com.ethosa.ktc.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.teacher.TeacherTimetable
import com.ethosa.ktc.databinding.LayoutTeacherLessonBinding
import com.ethosa.ktc.databinding.LayoutTimetableBinding
import com.ethosa.ktc.ui.fragments.TimetableFragment

/**
 * Implementation of TimetableAdapter but for teachers
 */
class TeacherTimetableAdapter(
    private val timetableFragment: TimetableFragment,
    private val items: TeacherTimetable,
) : RecyclerView.Adapter<TeacherTimetableAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutTimetableBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_timetable, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val day = items.week[position]

        binding.dayHeader.text = day.title

        // ох уж эти костыли ...
        val header = binding.dayRoot
        binding.root.removeAllViews()
        binding.root.addView(header)

        var lesson: LayoutTeacherLessonBinding
        for (l in day.lessons) {
            lesson = LayoutTeacherLessonBinding.inflate(
                LayoutInflater.from(timetableFragment.context),
                null, false)
            if (l.group == "") continue
            with (lesson) {
                tlessonClassroom.text = l.classroom
                tlessonGroup.text = l.group
                tlessonTitle.text = l.title
                tlessonNumber.text = l.number
                binding.root.addView(root)
            }
        }
    }

    override fun getItemCount(): Int = items.week.count()
}