package com.ethosa.ktc.ui.adapters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ethosa.ktc.R
import com.ethosa.ktc.college.objects.New
import com.ethosa.ktc.databinding.WallBinding
import com.ethosa.ktc.utils.CenterInsideBlur

class NewsAdapter(var items: List<New>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: WallBinding = WallBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.wall, parent, false)

        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        if (items[pos].image == "") {
            holder.binding.root.removeView(holder.binding.image)
        } else {
            Glide.with(holder.binding.root)
                .asBitmap()
                .load(items[pos].image)
                .transform(CenterInsideBlur(40, 5))
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        holder.binding.root.removeView(holder.binding.image)
                        return true
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        holder.binding.image.setImageDrawable(BitmapDrawable(resource))
                        return true
                    }

                })
                .into(holder.binding.image)
        }
        holder.binding.wallTitle.text = items[pos].title
        holder.binding.wallText.text = items[pos].body
        holder.binding.wallDate.text = items[pos].date
    }

    override fun getItemCount() = items.size
}