package com.ethosa.ktc.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R
import com.ethosa.ktc.ui.activities.WallPostActivity
import com.ethosa.ktc.college.news.News
import com.ethosa.ktc.databinding.LayoutStoryBinding

/**
 * Provides RecyclerView.Adapter behavior for news
 */
class NewsStoriesAdapter(
    private var items: List<News>
) : RecyclerView.Adapter<NewsStoriesAdapter.ViewHolder>(){

    /**
     * Provides RecyclerView.ViewHolder behavior
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = LayoutStoryBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_story, parent, false)
        )
    }

    /**
     * Loads image if available (without caching)
     * Loads title, date and wall post text
     */
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val binding = holder.binding
        val new = items[pos]
        new.catch()

        // Download image and blurs it.
        Glide.with(binding.root)
            .load(new.image)
            .into(binding.story)

        if (new.id in Preferences.viewedNews)
            binding.story.strokeWidth = 0f

        binding.root.setOnClickListener {
            // Go to WallPostActivity
            binding.story.strokeWidth = 0f
            val intent = Intent(binding.root.context, WallPostActivity::class.java)
            intent.putExtra("id", new.id)
            intent.putExtra("title", new.title)
            intent.putExtra("image", new.image)
            binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}