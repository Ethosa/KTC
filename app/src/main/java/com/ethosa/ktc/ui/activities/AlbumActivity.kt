package com.ethosa.ktc.ui.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.gallery.Album
import com.ethosa.ktc.databinding.ActivityAlbumBinding
import com.ethosa.ktc.ui.adapters.AlbumAdapter
import com.ethosa.ktc.ui.decoration.SpacingItemDecoration
import com.ethosa.ktc.utils.AppDynamicTheme
import com.google.gson.Gson
import jp.wasabeef.glide.transformations.BlurTransformation
import okhttp3.Call
import okhttp3.Response

/**
 * Provides album behavior.
 */
class AlbumActivity(
    private val spanCount: Int = 2
) : AppCompatActivity(), CollegeCallback {

    lateinit var binding: ActivityAlbumBinding
    private val college = CollegeApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        AppDynamicTheme(this).loadTheme()

        // Setup recycler view
        val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.albumContent.album.layoutManager = layoutManager
        binding.albumContent.album.setHasFixedSize(true)
        binding.albumContent.album.addItemDecoration(SpacingItemDecoration(8))

        // Fetch intent extra data
        binding.toolbarLayout.title = intent.getStringExtra("title")
        Glide.with(binding.root)
            .load(intent.getStringExtra("preview"))
            .transform(BlurTransformation(20, 2))
            .into(binding.albumToolbarImage)
        college.fetchAlbumById(intent.getStringExtra("id")!!, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    /**
     * Fetches album content
     */
    override fun onResponse(call: Call, response: Response) {
        // Parse JSON
        val json = response.body?.string()
        val album = Gson().fromJson(json, Album::class.java)

        runOnUiThread  {
            binding.albumContent.album.adapter = AlbumAdapter(album, this)
        }
    }
}