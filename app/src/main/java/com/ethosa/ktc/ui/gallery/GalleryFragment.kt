package com.ethosa.ktc.ui.gallery

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.interfaces.CollegeCallback
import com.ethosa.ktc.college.objects.gallery.Albums
import com.ethosa.ktc.databinding.FragmentGalleryBinding
import com.ethosa.ktc.ui.adapters.AlbumsPreviewAdapter
import com.ethosa.ktc.utils.SpacingItemDecoration
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response


class GalleryFragment(
    private val spanCount: Int = 2
) : Fragment(), CollegeCallback {

    private var _binding: FragmentGalleryBinding? = null
    private val college = CollegeApi()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.albumsList.layoutManager = layoutManager
        binding.albumsList.setHasFixedSize(true)
        binding.albumsList.addItemDecoration(SpacingItemDecoration(8))

        college.fetchAlbums(this)

        return binding.root
    }

    /**
     * Destroy bindings.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Called when KTC Api successfully fetches albums.
     */
    override fun onResponse(call: Call, response: Response) {
        if (_binding == null) return
        // Parse JSON
        val json = response.body?.string()
        val albums = Gson().fromJson(json, Albums::class.java)
        // Create animation object
        val animate = ObjectAnimator.ofFloat(
            binding.progressLoad, "alpha",
            1f,
            0f
        )
        animate.duration = 500
        activity?.runOnUiThread {
            binding.albumsList.adapter = AlbumsPreviewAdapter(albums)
            animate.start()
        }
    }
}