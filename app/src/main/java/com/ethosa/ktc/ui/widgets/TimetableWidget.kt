package com.ethosa.ktc.ui.widgets

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.timetable.Week
import com.ethosa.ktc.ui.activities.MainActivity
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import java.util.*


val college = CollegeApi()
var preferences: SharedPreferences? = null


/**
 * Implementation of App Widget functionality.
 */
class TimetableWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        preferences = context.getSharedPreferences("com.ethosa.ktc", Context.MODE_PRIVATE)
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}


/**
 * Update timetable for current widget
 */
@SuppressLint("UnspecifiedImmutableFlag")
internal fun updateWidgetPendingIntent(
    context: Context?,
    appWidgetId: Int
): PendingIntent? {
    val intent = Intent(context, TimetableWidget::class.java)
    val ids = AppWidgetManager.getInstance(context)
        .getAppWidgetIds(ComponentName(context!!, TimetableWidget::class.java))
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
    return PendingIntent.getBroadcast(
        context,
        appWidgetId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)
}


/**
 * Open app with pending intent
 */
@SuppressLint("UnspecifiedImmutableFlag")
internal fun openAppPendingIntent(
    context: Context?,
    appWidgetId: Int
): PendingIntent? {
    val intent = Intent(context, MainActivity::class.java)
    return PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}


@SuppressLint("RemoteViewLayout")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.timetable_widget)
    views.setOnClickPendingIntent(
        R.id.timetable_widget_reload,
        updateWidgetPendingIntent(context, appWidgetId)
    )
    views.setOnClickPendingIntent(
        R.id.timetable_widget_background,
        openAppPendingIntent(context, appWidgetId)
    )
    // Load last group ID
    val groupId = preferences?.getInt("group", 0)
    val calendar = Calendar.getInstance()
    val weekday = calendar.get(Calendar.DAY_OF_WEEK)

    college.fetchTimetable(groupId!!, object : CollegeCallback {
        @SuppressLint("SetTextI18n")
        override fun onResponse(call: Call, response: Response) {
            // Parse JSON
            val json = response.body?.string()
            val timetable = Gson().fromJson(json, Week::class.java)
            // Get current day timetable
            val day =
                if (weekday > 1)
                    timetable.days[weekday-2]
                else
                    timetable.days[0]

            views.setTextViewText(R.id.timetable_widget_title, day.title)
            views.removeAllViews(R.id.timetable_widget_lessons)

            // Setup view
            for (l in day.lessons) {
                println(l)
                // Load lesson data
                val lesson = RemoteViews(context.packageName, R.layout.widget_lesson)
                lesson.setTextViewText(R.id.lesson_title, l.title)
                lesson.setTextViewText(R.id.lesson_classroom, l.classroom)
                lesson.setTextViewText(R.id.lesson_number, l.time[0])
                lesson.setTextViewText(R.id.lesson_from, l.time[1])
                lesson.setTextViewText(R.id.lesson_to, l.time[2])
                lesson.setTextViewText(R.id.lesson_teacher, l.teacher)
                views.addView(R.id.timetable_widget_lessons, lesson)
                // Update widget
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }, null)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}