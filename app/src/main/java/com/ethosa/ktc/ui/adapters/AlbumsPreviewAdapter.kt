package com.ethosa.ktc.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.activities.AlbumActivity
import com.ethosa.ktc.college.objects.gallery.AlbumPreview
import com.ethosa.ktc.databinding.AlbumPreviewBinding

class AlbumsPreviewAdapter(
    private val items: List<AlbumPreview>
) : RecyclerView.Adapter<AlbumsPreviewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = AlbumPreviewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_preview, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val binding = holder.binding
        val album = items[pos]
        binding.albumTitle.text = album.title
        // Download image
        Glide.with(binding.root)
            .load(album.preview)
            .into(binding.preview)

        binding.holder.setOnClickListener {
            val intent = Intent(binding.root.context, AlbumActivity::class.java)
            intent.putExtra("id", album.id)
            intent.putExtra("preview", album.preview)
            intent.putExtra("title", album.title)
            binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}