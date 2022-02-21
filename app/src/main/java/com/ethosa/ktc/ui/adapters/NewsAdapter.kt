package com.ethosa.ktc.ui.adapters

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
import com.ethosa.ktc.activities.WallPostActivity
import com.ethosa.ktc.college.objects.news.News
import com.ethosa.ktc.databinding.LayoutWallBinding
import com.ethosa.ktc.interfaces.GlideCallback
import com.ethosa.ktc.glide.transformation.CenterInsideBlur
import com.ethosa.ktc.glide.GlideListener
import java.lang.Exception


class NewsAdapter(private var items: List<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = LayoutWallBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_wall, parent, false)
        return ViewHolder(layoutInflater)
    }

    /**
     * Loads image if available (without caching)
     * Loads title, date and wall post text
     */
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

        binding.root.setOnClickListener {
            // Go to WallPostActivity
            val intent = Intent(binding.root.context, WallPostActivity::class.java)
            intent.putExtra("id", new.id)
            intent.putExtra("title", new.title)
            intent.putExtra("image", new.image)
            binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}