package com.ethosa.ktc.ui.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.ui.activities.WallPostActivity
import com.ethosa.ktc.college.news.News
import com.ethosa.ktc.databinding.LayoutNewsBinding
import com.ethosa.ktc.glide.GlideCallback
import com.ethosa.ktc.glide.transformation.CenterInsideBlur
import com.ethosa.ktc.glide.GlideListener
import com.ethosa.ktc.ui.dialog.AppUpdater
import java.lang.Exception

/**
 * Provides RecyclerView.Adapter behavior for news
 */
class NewsAdapter(
    private var items: List<News>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    /**
     * Provides RecyclerView.ViewHolder behavior
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = LayoutNewsBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_news, parent, false)
        )
    }

    /**
     * Loads image if available (without caching)
     * Loads title, date and wall post text
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val binding = holder.binding
        val new = items[pos]
        if (new.image == "") {
            binding.root.removeView(binding.image)
        } else {
            // Download image and blurs it.
            Glide.with(binding.root)
                .asBitmap()
                .load(new.image)
                .transform(CenterInsideBlur(40, 5))
                .listener(GlideListener(
                    object : GlideCallback {
                        override fun onReady(res: Bitmap) {
                            binding.image.setImageDrawable(
                                BitmapDrawable(Resources.getSystem(), res)
                            )
                        }

                        override fun onFailure(e: Exception) {
                            binding.root.removeView(binding.image)
                        }
                    }
                ))
                .into(binding.image)
        }
        binding.wallTitle.text = new.title
        binding.wallText.text = new.body
        binding.wallDate.text = new.date
        binding.likeCount.text = new.likes.size.toString()
        if (items[pos].likes.contains(AppUpdater.UUID))
            binding.like.setImageResource(R.drawable.ic_heart_liked)

        binding.root.setOnClickListener {
            // Go to WallPostActivity
            val intent = Intent(binding.root.context, WallPostActivity::class.java)
            intent.putExtra("image", new.image)
            intent.putExtra("id", new.id)
            intent.putExtra("title", new.title)
            binding.root.context.startActivity(intent)
        }

        binding.like.setOnClickListener {
            CollegeApi.likeNewsById(new.id.toInt(), binding.root.context)
            if (items[pos].likes.contains(AppUpdater.UUID)) {
                binding.likeCount.text = (items[pos].likes.size - 1).toString()
                items[pos].likes = items[pos].likes.minus(AppUpdater.UUID)
                binding.like.setImageResource(R.drawable.ic_heart)
            }
            else {
                binding.likeCount.text = (items[pos].likes.size + 1).toString()
                items[pos].likes = items[pos].likes.plus(AppUpdater.UUID)
                binding.like.setImageResource(R.drawable.ic_heart_liked)
            }
        }
    }

    override fun getItemCount() = items.size
}