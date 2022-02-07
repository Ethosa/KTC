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
import com.ethosa.ktc.R
import com.ethosa.ktc.college.objects.New
import com.ethosa.ktc.databinding.FragmentHomeBinding
import com.ethosa.ktc.ui.adapters.NewsAdapter

class HomeFragment : Fragment() {

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
        val items: ArrayList<New> = ArrayList()
        for (i in 0 until 10)
            items.add(New("Lorem Ipsum", getString(R.string.lorem_ipsum)))

        val itemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
        binding.newsContainer.layoutManager = LinearLayoutManager(context)
        binding.newsContainer.adapter = NewsAdapter(items)
        binding.newsContainer.addItemDecoration(itemDecoration)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}