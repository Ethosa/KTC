package com.ethosa.ktc.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.objects.timetable.Week
import com.ethosa.ktc.databinding.LessonBinding
import com.ethosa.ktc.databinding.TimetableBinding
import com.ethosa.ktc.ui.timetable.TimetableFragment

class TimetableAdapter(
    private val timetableFragment: TimetableFragment,
    private val week: Week
) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TimetableBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.timetable, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val day = week.days[position]

        binding.dayHeader.text = day.title

        for (l in day.lessons) {
            val lesson = LessonBinding.inflate(
                LayoutInflater.from(timetableFragment.context),
                holder.binding.root, false)
            lesson.lessonTitle.text = l.title
            lesson.lessonClassroom.text = l.auditory
            lesson.lessonNumber.text = l.time[0]
            lesson.lessonFrom.text = l.time[1]
            lesson.lessonTo.text = l.time[2]
            lesson.lessonTeacher.text = l.teacher
            binding.root.addView(lesson.root)
        }
    }

    override fun getItemCount(): Int = week.days.count()
}