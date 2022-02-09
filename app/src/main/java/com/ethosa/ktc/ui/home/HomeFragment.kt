package com.ethosa.ktc.ui.home

import android.animation.ObjectAnimator
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
import com.ethosa.ktc.college.objects.news.LastNews
import com.ethosa.ktc.databinding.FragmentHomeBinding
import com.ethosa.ktc.ui.adapters.NewsAdapter
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

class HomeFragment : Fragment(), CollegeCallback {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val itemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.newsContainer.layoutManager = LinearLayoutManager(context)
        binding.newsContainer.addItemDecoration(itemDecoration)
        // Fetch last news from college
        val college = CollegeApi()
        college.lastNews(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Calls when collegeApi fetches last news.
     */
    override fun onResponse(call: Call, response: Response) {
        // Parse JSON
        val jsonString = response.body?.string()
        val news = Gson().fromJson(jsonString, LastNews::class.java)
        // Create animation object
        val animate = ObjectAnimator.ofFloat(
            binding.progessLoad, "alpha",
            1f,
            0f
        )
        animate.duration = 500
        activity?.runOnUiThread {
            binding.newsContainer.adapter = NewsAdapter(news.anonce)
            animate.start()
        }
    }
}