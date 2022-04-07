package com.ethosa.ktc.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.teacher.TeacherTimetable
import com.ethosa.ktc.college.teacher.TeachersList
import com.ethosa.ktc.college.timetable.*
import com.ethosa.ktc.databinding.FragmentTimetableBinding
import com.ethosa.ktc.ui.adapters.*
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
    private lateinit var preferences: Preferences
    private lateinit var itemDecoration: RecyclerView.ItemDecoration

    private val college = CollegeApi()
    private val binding get() = _binding!!

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
        preferences = Preferences(requireContext())
        loadState()

        // Analog for back button
        binding.back.setOnClickListener {
            // back button disabled when state isn't 0
            binding.back.isEnabled = !onBackPressed()
        }

        // Next week
        binding.next.setOnClickListener {
            if (Preferences.week < 57)
                changeWeek(1)
        }

        // previous week
        binding.previous.setOnClickListener {
            if (Preferences.week > 1)
                changeWeek(-1)
        }

        // toggle isStudent
        binding.toggleTimetable.setOnClickListener {
            Preferences.isStudent = !Preferences.isStudent
            loadState()
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
        when (Preferences.timetableState) {
            1 -> fetchBranches()
            2 ->
                if (Preferences.isStudent)
                    fetchCourses(Preferences.branch!!.id)
                else
                    fetchTeacherList(Preferences.branch!!.id)
            else -> return false
        }
        return true
    }

    private fun changeWeek(i: Int) {
        // Provides week changing behavior.
        Preferences.week += i
        binding.next.isEnabled = false
        binding.previous.isEnabled = false
        fetchTimetable(Preferences.group!!.id, Preferences.week)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadState() {
        // Fetches timetable from loaded state.
        when (Preferences.timetableState) {
            // branches
            0 -> fetchBranches()
            // courses
            1 ->
                if (Preferences.isStudent)
                    fetchCourses(Preferences.branch!!.id)
                else
                    fetchTeacherList(Preferences.branch!!.id)
            // students timetable
            2 ->
                if (Preferences.isStudent)
                    fetchTimetable(Preferences.group!!.id)
                else
                    fetchTeacherTimetable(Preferences.teacherId)
        }
        binding.toggleTimetable.setImageDrawable(resources.getDrawable(
            if (Preferences.isStudent) R.drawable.ic_graduation_cap else R.drawable.ic_globe_alt,
            requireContext().theme))
    }

    /**
     * Fetches branches and shows it.
     */
    private fun fetchBranches() {
        Preferences.timetableState = 0
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
                    preferences.saveTimetable()
                }
            }
        })
    }

    /**
     * Fetches courses for specified branch.
     * @param branchId unique branch ID.
     */
    fun fetchCourses(branchId: Int) {
        Preferences.timetableState = 1
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
                    preferences.saveTimetable()
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
        Preferences.timetableState = 2
        college.fetchTimetable(groupId, object : CollegeCallback {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val timetable = Gson().fromJson(json, Week::class.java)
                Preferences.week = timetable.week_number

                requireActivity().runOnUiThread {
                    binding.back.isEnabled = true
                    binding.next.isEnabled = true
                    binding.previous.isEnabled = true
                    binding.timetableTitle.text = "${Preferences.group!!.title}\n${timetable.week_number} неделя"
                    binding.timetableToolbar.visibility = View.VISIBLE
                    binding.next.visibility = View.VISIBLE
                    binding.previous.visibility = View.VISIBLE
                    binding.timetable.adapter = TimetableAdapter(this@TimetableFragment, timetable)
                    preferences.saveTimetable()
                }
            }
        }, week)
    }

    /**
     * Fetches timetable for specified teacher.
     * @param teacherId unique teacher ID.
     */
    fun fetchTeacherTimetable(teacherId: Int = -1) {
        Preferences.timetableState = 2
        college.fetchTeacherTimetable(
            Preferences.branch!!.id,
            if (teacherId < 0) Preferences.teacherId else teacherId,
            object : CollegeCallback {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call, response: Response) {
                    if (_binding == null) return
                    // Parse JSON
                    val json = response.body?.string()
                    val timetable = Gson().fromJson(json, TeacherTimetable::class.java)
                    Preferences.teacherId = teacherId

                    requireActivity().runOnUiThread {
                        binding.back.isEnabled = true
                        binding.timetableTitle.text = timetable.title
                        binding.timetableToolbar.visibility = View.VISIBLE
                        binding.next.visibility = View.GONE
                        binding.previous.visibility = View.GONE
                        binding.timetable.adapter = TeacherTimetableAdapter(this@TimetableFragment, timetable)
                        preferences.saveTimetable()
                    }
                }
        })
    }

    /**
     * Fetches list of teachers
     * @param branchId unique branch ID.
     */
    private fun fetchTeacherList(branchId: Int) {
        Preferences.timetableState = 1
        college.fetchTeachersList(branchId, object : CollegeCallback {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val teachers = Gson().fromJson(json, TeachersList::class.java)
                teachers.teachers.removeAt(0)

                requireActivity().runOnUiThread {
                    binding.back.isEnabled = true
                    binding.timetableToolbar.visibility = View.VISIBLE
                    binding.next.visibility = View.GONE
                    binding.previous.visibility = View.GONE
                    binding.timetableTitle.text = "Список учителей"
                    binding.timetable.adapter = TeachersListAdapter(this@TimetableFragment, teachers)
                    preferences.saveTimetable()
                }
            }
        })
    }
}