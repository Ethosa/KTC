package com.ethosa.ktc.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.news.LastNews
import com.ethosa.ktc.databinding.FragmentNewsBinding
import com.ethosa.ktc.ui.adapters.NewsAdapter
import com.ethosa.ktc.ui.adapters.NewsStoriesAdapter
import com.ethosa.ktc.ui.decoration.SpacingItemDecoration
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

/**
 * Provides working with KTC last news.
 */
class NewsFragment : Fragment(), CollegeCallback {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.news.layoutManager = LinearLayoutManager(context)
        binding.news.addItemDecoration(SpacingItemDecoration(0, 32))

        binding.newsStories.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.newsStories.addItemDecoration(SpacingItemDecoration(32, 0))

        // Fetch last news from college
        CollegeApi.fetchLastNews(this)

        return root
    }

    /**
     * destroy bindings.
     */
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
            _binding?.progressLoad, "alpha",
            1f, 0f
        ).setDuration(500)
        activity?.runOnUiThread {
            animate.start()
            _binding?.news?.adapter = NewsAdapter(news.anonce + news.news)
            _binding?.newsStories?.adapter = NewsStoriesAdapter(
                (news.anonce + news.news)
                    .filter { it.image != "" }
                    .sortedBy { it.id in Preferences.viewedNews }
            )
        }
    }
}