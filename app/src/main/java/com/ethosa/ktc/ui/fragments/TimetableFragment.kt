package com.ethosa.ktc.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.timetable.*
import com.ethosa.ktc.databinding.FragmentTimetableBinding
import com.ethosa.ktc.ui.adapters.BranchAdapter
import com.ethosa.ktc.ui.adapters.CourseAdapter
import com.ethosa.ktc.ui.adapters.TimetableAdapter
import com.ethosa.ktc.utils.IOFragmentBackPressed
import com.ethosa.ktc.ui.decoration.SpacingItemDecoration
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

/**
 * Provides working with KTC timetable.
 * Includes branches, courses with groups and timetable for any week.
 */
class TimetableFragment : IOFragmentBackPressed() {
    private var _binding: FragmentTimetableBinding? = null
    private lateinit var itemDecoration: RecyclerView.ItemDecoration
    private lateinit var preferences: SharedPreferences

    private val college = CollegeApi()
    private val binding get() = _binding!!
    // 0 - branches, 1 - courses, 2 - timetable.
    private var state = 0
    var branch: Branch? = null
    var group: Group? = null
    var week: Int = 0
    var isStudent = true

    companion object {
        const val STATE = "state"
        const val BRANCH = "branch"
        const val GROUP = "group"
        const val GROUP_TITLE = "group_title"
        const val WEEK = "week"
        const val IS_STUDENT = "is_student"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)

        binding.timetable.layoutManager = LinearLayoutManager(context)
        itemDecoration = SpacingItemDecoration(0, 32)
        binding.timetable.addItemDecoration(itemDecoration)

        // Load state
        preferences = requireActivity().getSharedPreferences("com.ethosa.ktc", Context.MODE_PRIVATE)
        state = preferences.getInt(STATE, 0)
        branch = Branch(preferences.getInt(BRANCH, 0), "")
        group = Group(
            preferences.getInt(GROUP, 0),
            preferences.getString(GROUP_TITLE, "")!!
        )
        week = preferences.getInt(WEEK, 0)
        isStudent = preferences.getBoolean(IS_STUDENT, true)
        loadState()

        // Analog for back button
        binding.back.setOnClickListener {
            // back button disabled when state isn't 0
            binding.back.isEnabled = !onBackPressed()
        }

        // Next week
        binding.next.setOnClickListener {
            if (week < 57)
                changeWeek(1)
        }

        // previous week
        binding.previous.setOnClickListener {
            if (week > 1)
                changeWeek(-1)
        }

        return binding.root
    }

    /**
     * destroy bindings.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Calls when back button pressed.
     */
    override fun onBackPressed(): Boolean {
        // Provides behavior on Back pressed.
        when (state) {
            1 -> fetchBranches()
            2 -> fetchCourses(branch!!.id)
            else -> return false
        }
        return true
    }

    private fun changeWeek(i: Int) {
        // Provides week changing behavior.
        week += i
        binding.next.isEnabled = false
        binding.previous.isEnabled = false
        fetchTimetable(group!!.id, week)
    }

    private fun loadState() {
        // Fetches timetable from loaded state.
        when (state) {
            0 -> fetchBranches()
            1 -> fetchCourses(branch!!.id)
            2 -> fetchTimetable(group!!.id)
        }
    }

    private fun updateState(current: Int = 0) {
        // Updates fragment's state and saves it into SharedPreferences
        state = current
        preferences.edit().putInt(STATE, state).apply()
    }

    private fun updateTimetable(timetable: Week) {
        // Updates fragment's group and saves it into SharedPreferences
        preferences.edit().putInt(GROUP, group!!.id).apply()
        preferences.edit().putInt(WEEK, timetable.week_number).apply()
        preferences.edit().putString(GROUP_TITLE, group!!.title).apply()
    }

    /**
     * Fetches branches and shows it.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun fetchBranches() {
        updateState()
        college.fetchBranches(object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val branches = Gson().fromJson(json, Branches::class.java)

                requireActivity().runOnUiThread {
                    binding.back.isEnabled = true
                    binding.timetableToolbar.visibility = View.GONE
                    binding.timetable.adapter = BranchAdapter(this@TimetableFragment, branches)
                }
            }
        })
    }

    /**
     * Fetches courses for specified branch.
     * @param branchId unique branch ID.
     */
    fun fetchCourses(branchId: Int) {
        updateState(1)
        college.fetchCourses(branchId, object : CollegeCallback {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val courses = Gson().fromJson(json, Courses::class.java)

                requireActivity().runOnUiThread {
                    binding.back.isEnabled = true
                    binding.timetableToolbar.visibility = View.VISIBLE
                    binding.next.visibility = View.GONE
                    binding.previous.visibility = View.GONE
                    binding.timetableTitle.text = "Курсы"
                    binding.timetable.adapter = CourseAdapter(this@TimetableFragment, courses)
                    preferences.edit().putInt(BRANCH, branch!!.id).apply()
                }
            }
        })
    }

    /**
     * Fetches timetable for specified groupId.
     * @param groupId unique group ID.
     * @param week by default is current week.
     */
    fun fetchTimetable(groupId: Int, week: Int? = null) {
        updateState(2)
        college.fetchTimetable(groupId, object : CollegeCallback {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val timetable = Gson().fromJson(json, Week::class.java)
                this@TimetableFragment.week = timetable.week_number

                requireActivity().runOnUiThread {
                    binding.back.isEnabled = true
                    binding.next.isEnabled = true
                    binding.previous.isEnabled = true
                    binding.timetableTitle.text = "${group!!.title}\n${timetable.week_number} неделя"
                    binding.timetableToolbar.visibility = View.VISIBLE
                    binding.next.visibility = View.VISIBLE
                    binding.previous.visibility = View.VISIBLE
                    binding.timetable.adapter = TimetableAdapter(this@TimetableFragment, timetable)
                    updateTimetable(timetable)
                }
            }
        }, week)
    }
}