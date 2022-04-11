package com.ethosa.ktc.ui.activities

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.college.news.News
import com.ethosa.ktc.databinding.ActivityWallPostBinding
import com.ethosa.ktc.glide.transformation.CenterInsideBlur
import com.ethosa.ktc.utils.AppDynamicTheme
import com.ethosa.ktc.utils.HtmlImageGetter
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

/**
 * Provides Wall post behavior
 */
class WallPostActivity : AppCompatActivity(), CollegeCallback {

    private lateinit var binding: ActivityWallPostBinding
    private val college: CollegeApi = CollegeApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup view binding
        binding = ActivityWallPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        AppDynamicTheme(this).loadTheme()

        // Loads intent data to toolbarLayout
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = intent.getStringExtra("title")
        Glide.with(binding.root)
            .asBitmap()
            .load(intent.getStringExtra("image"))
            .transform(CenterInsideBlur(40, 5))
            .into(binding.toolbarImage)

        // Fetches post data.
        college.fetchNewById(intent.getStringExtra("id")!!.toInt(), this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    /**
     * Fetches a news data
     */
    override fun onResponse(call: Call, response: Response) {
        // Parse JSON
        val body = response.body?.string()
        val new = Gson().fromJson(body, News::class.java)
        // Create animation object
        val animate = ObjectAnimator.ofFloat(
            binding.content.progressBar, "alpha", 1f, 0f
        ).setDuration(500)
        // Fix <img/> tag
        new.body = new.body.replace(
            "src=\"/", "src=\"http://www.kansk-tc.ru/"
        )
        runOnUiThread {
            // Render HTML tags
            binding.content.body.text = HtmlCompat.fromHtml(
                new.body,
                HtmlCompat.FROM_HTML_MODE_LEGACY,
                HtmlImageGetter(resources, binding.content.body),
                null
            )
            // cancel progress
            animate.start()
        }
    }
}