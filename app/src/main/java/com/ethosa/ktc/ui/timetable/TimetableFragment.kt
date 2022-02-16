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
import com.ethosa.ktc.databinding.FragmentTimetableBinding
import com.ethosa.ktc.ui.adapters.BranchAdapter
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

class TimetableFragment : Fragment() {
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

        college.fetchBranches(branchCallback)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val branchCallback = object : CollegeCallback  {
        override fun onResponse(call: Call, response: Response) {
            if (_binding == null) return
            // Parse JSON
            val json = response.body?.string()
            val branches = Gson().fromJson(json, Branches::class.java)

            requireActivity().runOnUiThread {
                binding.timetable.adapter = BranchAdapter(branches)
            }
        }
    }
}