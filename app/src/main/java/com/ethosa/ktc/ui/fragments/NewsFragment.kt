package com.ethosa.ktc.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.news.LastNewsBeta
import com.ethosa.ktc.databinding.FragmentNewsBinding
import com.ethosa.ktc.ui.adapters.NewsAdapter
import com.ethosa.ktc.ui.adapters.NewsStoriesAdapter
import com.ethosa.ktc.ui.decoration.SpacingItemDecoration
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

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

        binding.newsStories.layoutManager = LinearLayoutManager(
            context, RecyclerView.HORIZONTAL, false
        )
        binding.newsStories.addItemDecoration(SpacingItemDecoration(16, 0))

        // Fetch last news from college
        CollegeApi.fetchLastNewsBeta(this)

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
    @SuppressLint("Recycle")
    override fun onResponse(call: Call, response: Response) {
        // Parse JSON
        val jsonString = response.body?.string()
        val news: LastNewsBeta
        try {
            news = Gson().fromJson(jsonString, LastNewsBeta::class.java)
        } catch (e: JsonSyntaxException) {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(), R.string.toast_news_error, Toast.LENGTH_SHORT
                ).show()
            }
            return
        } catch (e: Exception) {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(), R.string.toast_unknown_error, Toast.LENGTH_SHORT
                ).show()
            }
            e.printStackTrace()
            return
        }
        activity?.runOnUiThread {
            _binding?.news?.adapter = NewsAdapter(news.announce + news.news)
            _binding?.newsStories?.adapter = NewsStoriesAdapter(
                (news.announce + news.news)
                    .filter { it.image != "" }
                    .sortedBy { it.id in Preferences.viewedNews }
            )
            // Animation
            _binding?.progressLoad?.let {
                ViewCompat.animate(it)
                    .setDuration(500)
                    .alpha(0f)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            }
            // stories
            _binding?.newsStories?.let {
                it.translationX = 500f
                it.alpha = 0f
                ViewCompat.animate(it)
                    .setDuration(500)
                    .translationX(0f)
                    .setInterpolator(DecelerateInterpolator())
                    .alpha(1f)
                    .startDelay = 800
            }
            // news
            _binding?.news?.let {
                it.translationY = 500f
                it.alpha = 0f
                ViewCompat.animate(it)
                    .setDuration(500)
                    .translationY(0f)
                    .setInterpolator(DecelerateInterpolator())
                    .alpha(1f)
                    .startDelay = 800
            }
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        super.onFailure(call, e)
        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(), R.string.toast_news_error, Toast.LENGTH_SHORT
            ).show()
        }
    }
}