package com.ethosa.ktc.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.gallery.Albums
import com.ethosa.ktc.databinding.FragmentGalleryBinding
import com.ethosa.ktc.ui.adapters.AlbumsPreviewAdapter
import com.ethosa.ktc.ui.decoration.SpacingItemDecoration
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

/**
 * Provides albums behavior
 */
class GalleryFragment(
    private val spanCount: Int = 2
) : Fragment(), CollegeCallback {

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val layoutManager = StaggeredGridLayoutManager(
            spanCount, StaggeredGridLayoutManager.VERTICAL
        )
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.albumsList.layoutManager = layoutManager
        binding.albumsList.setHasFixedSize(true)
        binding.albumsList.addItemDecoration(SpacingItemDecoration(8))

        CollegeApi.fetchAlbums(this)

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
        // Parse JSON
        val json = response.body?.string()
        val albums: Albums
        try {
            albums = Gson().fromJson(json, Albums::class.java)
        } catch (e: JsonSyntaxException) {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(), R.string.toast_gallery_error, Toast.LENGTH_SHORT
                ).show()
            }
            return
        } catch (e: Exception) {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(), R.string.toast_unknown_error, Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        // Create animation object
        val animate = ObjectAnimator.ofFloat(
            _binding?.progressLoad, "alpha",
            1f,
            0f
        ).setDuration(500)
        activity?.runOnUiThread {
            _binding?.albumsList?.adapter = AlbumsPreviewAdapter(albums)
            animate.start()
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        super.onFailure(call, e)
        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(), R.string.toast_gallery_error, Toast.LENGTH_SHORT
            ).show()
        }
    }
}