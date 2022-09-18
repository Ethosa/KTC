package com.ethosa.ktc.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.R
import com.ethosa.ktc.college.timetable.Day
import com.ethosa.ktc.college.timetable.Week
import com.ethosa.ktc.databinding.LayoutLessonBinding
import com.ethosa.ktc.databinding.LayoutTimetableBinding
import com.ethosa.ktc.ui.fragments.TimetableFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Provides RecyclerView.Adapter behavior for timetable.
 */
class TimetableAdapter(
    private val timetableFragment: TimetableFragment,
    private val week: Week
) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {
    @SuppressLint("SimpleDateFormat")
    private var dateFormat = SimpleDateFormat("mm:ss")
    private var weekday: Day? = null
    private var now = "00:00"
    var currentPos: Int? = null
    /**
     * Provides RecyclerView.ViewHolder behavior.
     * Also includes TimetableBinding.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutTimetableBinding.bind(view)
    }

    init {
        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        val firstDay = calendar.firstDayOfWeek
        if (firstDay == Calendar.SUNDAY) {
            if (day == 1)
                day = 7
            else
                day--
        }
        now = "${calendar.get(Calendar.HOUR_OF_DAY)}:${Calendar.MINUTE}"
        currentPos = when (day) {
            7 -> null
            1 -> 0
            else -> day-1
        }
        weekday = when (day) {
            7 -> null
            1 -> week.days[0]
            else -> week.days[day-1]
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_timetable, parent, false))
    }

    /**
     * Binds every day in week.
     * Binds every lesson in day.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = week.days[position]
        holder.binding.dayHeader.text = day.title

        var lesson: LayoutLessonBinding
        for (l in day.lessons) {
            lesson = LayoutLessonBinding.inflate(
                LayoutInflater.from(timetableFragment.context),
                holder.binding.root, false)
            with (lesson) {
                lessonTitle.text = l.title
                lessonClassroom.text = l.classroom
                lessonNumber.text = l.time[0]
                lessonFrom.text = l.time[1]
                lessonTo.text = l.time[2]
                lessonTeacher.text = l.teacher

                // check time
                val from = dateFormat.parse(l.time[1])?.time!!
                val current = dateFormat.parse(now)?.time!!
                val to = dateFormat.parse(l.time[2])?.time!!
                if (current in from..to && weekday?.title == day.title)
                    lesson.root.setBackgroundResource(R.color.current_lesson)

                holder.binding.root.addView(root)
            }
        }
    }

    /**
     * @return days count.
     */
    override fun getItemCount(): Int = week.days.count()
}