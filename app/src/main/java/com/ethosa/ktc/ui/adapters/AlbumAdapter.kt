package com.ethosa.ktc.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.AlbumImageBinding

class AlbumAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = AlbumImageBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_image, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val binding = holder.binding
        val image = items[pos]
        // Download image
        Glide.with(binding.root)
            .asBitmap()
            .load(image)
            .into(binding.imageView)
    }

    override fun getItemCount(): Int = items.size
}