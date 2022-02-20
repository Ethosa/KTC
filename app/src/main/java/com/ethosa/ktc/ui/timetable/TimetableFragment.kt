package com.ethosa.ktc.ui.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.interfaces.CollegeCallback
import com.ethosa.ktc.college.objects.timetable.Branches
import com.ethosa.ktc.college.objects.timetable.Courses
import com.ethosa.ktc.college.objects.timetable.Week
import com.ethosa.ktc.databinding.FragmentTimetableBinding
import com.ethosa.ktc.ui.adapters.BranchAdapter
import com.ethosa.ktc.ui.adapters.CourseAdapter
import com.ethosa.ktc.ui.adapters.TimetableAdapter
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

class TimetableFragment : Fragment(), IOBackPressed {
    private var _binding: FragmentTimetableBinding? = null

    private val binding get() = _binding!!
    private val college = CollegeApi()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)

        val itemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.timetable.layoutManager = LinearLayoutManager(context)
        binding.timetable.addItemDecoration(itemDecoration)

        fetchBranches()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun fetchBranches() {
        college.fetchBranches(object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val branches = Gson().fromJson(json, Branches::class.java)

                requireActivity().runOnUiThread {
                    binding.timetable.adapter = BranchAdapter(this@TimetableFragment, branches)
                }
            }
        })
    }

    fun fetchCourses(branchId: Int) {
        college.fetchCourses(branchId, object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val courses = Gson().fromJson(json, Courses::class.java)

                requireActivity().runOnUiThread {
                    binding.timetable.adapter = CourseAdapter(this@TimetableFragment, courses)
                }
            }
        })
    }

    fun fetchTimetable(groupId: Int, week: Int? = null) {
        college.fetchTimetable(groupId, object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                if (_binding == null) return
                // Parse JSON
                val json = response.body?.string()
                val timetable = Gson().fromJson(json, Week::class.java)

                requireActivity().runOnUiThread {
                    binding.timetable.adapter = TimetableAdapter(this@TimetableFragment, timetable)
                }
            }
        }, week)
    }

    override fun onBackPressed() {
        when (binding.timetable.adapter!!::class.java) {
            CourseAdapter::class.java -> {
                fetchBranches()
            }
        }
    }
}