package com.ethosa.ktc.ui.widgets

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.timetable.Week
import com.ethosa.ktc.ui.activities.MainActivity
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class TimetableWidget : AppWidgetProvider() {
    private var preferences: Preferences? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        preferences = Preferences(context)
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * Update timetable for current widget
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun updateWidgetPendingIntent(
        context: Context?,
        appWidgetId: Int
    ): PendingIntent {
        val intent = Intent(context, TimetableWidget::class.java)
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context!!, TimetableWidget::class.java))
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

        return PendingIntent.getBroadcast(
            context,
            appWidgetId,
            intent,
            when {
                Build.VERSION.SDK_INT >= 31 -> PendingIntent.FLAG_MUTABLE
                else -> PendingIntent.FLAG_UPDATE_CURRENT
            })
    }

    /**
     * Open app with pending intent
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun openAppPendingIntent(
        context: Context?,
        appWidgetId: Int
    ): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            appWidgetId,
            intent,
            when {
                Build.VERSION.SDK_INT >= 31 -> PendingIntent.FLAG_MUTABLE
                else -> PendingIntent.FLAG_UPDATE_CURRENT
            })
    }

    @SuppressLint("RemoteViewLayout", "SimpleDateFormat")
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.widget_timetable)
        views.setOnClickPendingIntent(
            R.id.timetable_widget_reload,
            updateWidgetPendingIntent(context, appWidgetId)
        )
        views.setOnClickPendingIntent(
            R.id.timetable_widget_background,
            openAppPendingIntent(context, appWidgetId)
        )
        // Load last group ID
        val groupId = Preferences.group.id
        val calendar = Calendar.getInstance()
        val weekday = calendar.get(Calendar.DAY_OF_WEEK)
        val now = "${calendar.get(Calendar.HOUR_OF_DAY)}:${Calendar.MINUTE}"

        val dateFormat = SimpleDateFormat("mm:ss")

        CollegeApi.fetchTimetable(groupId, object : CollegeCallback {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                // Parse JSON
                val json = response.body?.string()
                val timetable = Gson().fromJson(json, Week::class.java)
                println(weekday)
                // Get current day timetable
                val day =
                    when {
                        weekday >= 2 -> timetable.days[weekday-2]
                        weekday > 1 -> timetable.days[1]
                        else -> timetable.days[0]
                    }

                // Setup widget
                views.setTextViewText(R.id.timetable_widget_title, day.title)
                views.removeAllViews(R.id.timetable_widget_lessons)

                // Setup views
                var lesson: RemoteViews
                for (l in day.lessons) {
                    println(l)
                    // Load lesson data
                    lesson = RemoteViews(context.packageName, R.layout.widget_lesson)
                    with (lesson) {
                        setTextViewText(R.id.lesson_title, l.title)
                        setTextViewText(R.id.lesson_classroom, l.classroom)
                        setTextViewText(R.id.lesson_number, l.time[0])
                        setTextViewText(R.id.lesson_from, l.time[1])
                        setTextViewText(R.id.lesson_to, l.time[2])
                        setTextViewText(R.id.lesson_teacher, l.teacher)
                    }

                    // check time
                    val from = dateFormat.parse(l.time[1])?.time!!
                    val current = dateFormat.parse(now)?.time!!
                    val to = dateFormat.parse(l.time[2])?.time!!
                    if (current in from..to)
                        lesson.setInt(
                            R.id.lesson_background,
                            "setBackgroundResource",
                            R.color.foreground_alpha
                        )

                    views.addView(R.id.timetable_widget_lessons, lesson)
                }
                // Update widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }, null)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}