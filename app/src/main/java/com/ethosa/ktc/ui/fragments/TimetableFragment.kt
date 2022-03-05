package com.ethosa.ktc.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.timetable.*
import com.ethosa.ktc.databinding.FragmentTimetableBinding
import com.ethosa.ktc.ui.adapters.BranchAdapter
import com.ethosa.ktc.ui.adapters.CourseAdapter
import com.ethosa.ktc.ui.adapters.TimetableAdapter
import com.ethosa.ktc.utils.SpacingItemDecoration
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

/**
 * Provides working with KTC timetable.
 * Includes branches, courses with groups and timetable for any week.
 */
class TimetableFragment : Fragment() {
    private var _binding: FragmentTimetableBinding? = null
    private lateinit var itemDecoration: RecyclerView.ItemDecoration
    private lateinit var preferences: SharedPreferences

    private val college = CollegeApi()
    private val binding get() = _binding!!
    // 0 - branches, 1 - courses, 2 - timetable.
    private var state = 0
    var branch: Branch? = null
    var group: Group? = null

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
        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        state = preferences.getInt("state", 0)
        branch = Branch(id = preferences.getInt("branch", 0), "")
        group = Group(preferences.getInt("group", 0), preferences.getString("group_title", "")!!)
        loadState()

        binding.back.setOnClickListener {
            when (state) {
                1 -> fetchBranches()
                2 -> fetchCourses(branch!!.id)
                else -> return@setOnClickListener
            }
            binding.back.isEnabled = false
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

    private fun loadState() {
        when (state) {
            0 -> fetchBranches()
            1 -> fetchCourses(branch!!.id)
            2 -> fetchTimetable(group!!.id)
        }
    }

    /**
     * Fetches branches and shows it.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun fetchBranches() {
        state = 0
        preferences.edit().putInt("state", state).apply()
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
        state = 1
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
                    binding.timetableTitle.text = "Курсы"
                    binding.timetable.adapter = CourseAdapter(this@TimetableFragment, courses)
                    preferences.edit().putInt("state", state).apply()
                    preferences.edit().putInt("branch", branch!!.id).apply()
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
        state = 2
        college.fetchTimetable(groupId, object : CollegeCallback {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val timetable = Gson().fromJson(json, Week::class.java)

                requireActivity().runOnUiThread {
                    binding.back.isEnabled = true
                    binding.timetableTitle.text = "${group!!.title}\n${timetable.week_number} неделя"
                    binding.timetableToolbar.visibility = View.VISIBLE
                    binding.timetable.adapter = TimetableAdapter(this@TimetableFragment, timetable)
                    preferences.edit().putInt("state", state).apply()
                    preferences.edit().putInt("group", group!!.id).apply()
                    preferences.edit().putString("group_title", group!!.title).apply()
                }
            }
        }, week)
    }
}