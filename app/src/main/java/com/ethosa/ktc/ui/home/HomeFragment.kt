package com.ethosa.ktc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.objects.LastNews
import com.ethosa.ktc.databinding.FragmentHomeBinding
import com.ethosa.ktc.ui.adapters.NewsAdapter
import com.google.gson.Gson
import okhttp3.Response

class HomeFragment : Fragment(), CollegeCallback<Response> {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val itemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.newsContainer.layoutManager = LinearLayoutManager(context)
        binding.newsContainer.addItemDecoration(itemDecoration)
        val college = CollegeApi()
        college.lastNews(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResponse(response: Response) {
        val jsonString = response.body?.string()
        val news = Gson().fromJson(jsonString, LastNews::class.java)
        activity?.runOnUiThread {
            binding.newsContainer.adapter = NewsAdapter(news.anonce)
        }
    }
}