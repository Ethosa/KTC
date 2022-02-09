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
import com.ethosa.ktc.college.objects.news.New
import com.ethosa.ktc.databinding.WallBinding
import com.ethosa.ktc.glide.GlideCallback
import com.ethosa.ktc.glide.transformation.CenterInsideBlur
import com.ethosa.ktc.glide.GlideListener
import java.lang.Exception


class NewsAdapter(private var items: List<New>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: WallBinding = WallBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.wall, parent, false)
        return ViewHolder(layoutInflater)
    }

    /**
     * Loads image if available (without caching)
     * Loads title, date and wall post text
     */
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        if (items[pos].image == "") {
            holder.binding.root.removeView(holder.binding.image)
        } else {
            // Download image and blurs it.
            Glide.with(holder.binding.root)
                .asBitmap()
                .load(items[pos].image)
                .transform(CenterInsideBlur(40, 5))
                .listener(GlideListener(
                    object : GlideCallback {
                        override fun onReady(res: Bitmap) {
                            holder.binding.image.setImageDrawable(
                                BitmapDrawable(Resources.getSystem(), res)
                            )
                        }

                        override fun onFailure(e: Exception) {
                            holder.binding.root.removeView(holder.binding.image)
                        }
                    }
                ))
                .into(holder.binding.image)
        }
        holder.binding.wallTitle.text = items[pos].title
        holder.binding.wallText.text = items[pos].body
        holder.binding.wallDate.text = items[pos].date

        holder.binding.root.setOnClickListener {
            // Go to WallPostActivity
            val intent = Intent(holder.binding.root.context, WallPostActivity::class.java)
            intent.putExtra("id", items[pos].id)
            intent.putExtra("title", items[pos].title)
            intent.putExtra("image", items[pos].image)
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size
}