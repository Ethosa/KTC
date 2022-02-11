package com.ethosa.ktc.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.interfaces.CollegeCallback
import com.ethosa.ktc.college.objects.gallery.Album
import com.ethosa.ktc.databinding.ActivityAlbumBinding
import com.ethosa.ktc.ui.adapters.AlbumAdapter
import com.ethosa.ktc.ui.adapters.SpacingItemDecoration
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

class AlbumActivity(
    private val spanCount: Int = 2
) : AppCompatActivity(), CollegeCallback {

    private lateinit var binding: ActivityAlbumBinding
    private val college = CollegeApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.albumContent.album.layoutManager = layoutManager
        binding.albumContent.album.setHasFixedSize(true)
        binding.albumContent.album.addItemDecoration(SpacingItemDecoration(8))

        college.fetchAlbumById(intent.getStringExtra("id")!!, this)
    }

    override fun onResponse(call: Call, response: Response) {
        // Parse JSON
        val json = response.body?.string()
        val album = Gson().fromJson(json, Album::class.java)

        runOnUiThread  {
            binding.albumContent.album.adapter = AlbumAdapter(album)
        }
    }
}