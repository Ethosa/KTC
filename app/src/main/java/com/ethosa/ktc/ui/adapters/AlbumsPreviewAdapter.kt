package com.ethosa.ktc.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.ui.activities.AlbumActivity
import com.ethosa.ktc.college.gallery.AlbumPreview
import com.ethosa.ktc.databinding.LayoutAlbumPreviewBinding
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * Provides RecyclerView.Adapter behavior for album previews
 */
class AlbumsPreviewAdapter(
    private val items: List<AlbumPreview>
) : RecyclerView.Adapter<AlbumsPreviewAdapter.ViewHolder>() {
    /**
     * Provides RecyclerView.ViewHolder behavior.
     * Also includes AlbumPreviewBinding.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutAlbumPreviewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_album_preview, parent, false)
        return ViewHolder(inflater)
    }

    /**
     * Builds album preview
     */
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val binding = holder.binding
        val album = items[pos]
        binding.albumTitle.text = album.title
        // Download image
        Glide.with(binding.root)
            .load(album.preview)
            .transform(BlurTransformation(3, 2))
            .into(binding.preview)

        binding.holder.setOnClickListener {
            val intent = Intent(binding.root.context, AlbumActivity::class.java)
            intent.putExtra("id", album.id)
            intent.putExtra("preview", album.preview)
            intent.putExtra("title", album.title)
            binding.root.context.startActivity(intent)
        }
    }

    /**
     * @return albums count
     */
    override fun getItemCount(): Int = items.size
}