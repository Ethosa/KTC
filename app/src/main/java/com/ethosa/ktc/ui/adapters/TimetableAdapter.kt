package com.ethosa.ktc.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.timetable.Week
import com.ethosa.ktc.databinding.LayoutLessonBinding
import com.ethosa.ktc.databinding.LayoutTimetableBinding
import com.ethosa.ktc.ui.fragments.TimetableFragment

/**
 * Provides RecyclerView.Adapter behavior for timetable.
 */
class TimetableAdapter(
    private val timetableFragment: TimetableFragment,
    private val week: Week
) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {
    /**
     * Provides RecyclerView.ViewHolder behavior.
     * Also includes TimetableBinding.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutTimetableBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_timetable, parent, false)
        return ViewHolder(inflater)
    }

    /**
     * Binds every day in week.
     * Binds every lesson in day.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val day = week.days[position]

        binding.dayHeader.text = day.title

        for (l in day.lessons) {
            val lesson = LayoutLessonBinding.inflate(
                LayoutInflater.from(timetableFragment.context),
                holder.binding.root, false)
            lesson.lessonTitle.text = l.title
            lesson.lessonClassroom.text = l.classroom
            lesson.lessonNumber.text = l.time[0]
            lesson.lessonFrom.text = l.time[1]
            lesson.lessonTo.text = l.time[2]
            lesson.lessonTeacher.text = l.teacher
            binding.root.addView(lesson.root)
        }
    }

    /**
     * @return days count.
     */
    override fun getItemCount(): Int = week.days.count()
}