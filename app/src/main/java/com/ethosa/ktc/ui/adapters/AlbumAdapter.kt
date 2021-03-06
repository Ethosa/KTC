package com.ethosa.ktc.ui.adapters

import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.LayoutAlbumImageBinding
import com.ethosa.ktc.ui.dialog.AlbumPhotoDialog


/**
 * Provides RecyclerView.Adapter behavior for album photos.
 */
class AlbumAdapter(
    val items: List<String>,
    val activity: AppCompatActivity
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private var dialog = AlbumPhotoDialog(this)

    /**
     * Provides RecyclerView.ViewHolder behavior.
     * Also includes AlbumImageBinding.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = LayoutAlbumImageBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_album_image, parent, false)
        )
    }

    /**
     * Builds photo
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val image = items[position]
        // Download image
        Glide.with(binding.root)
            .load(image)
            .into(binding.imageView)

        // Show photo dialog.
        binding.root.setOnClickListener {
            dialog.showImage(image, position)
        }
    }

    /**
     * @return image urls count
     */
    override fun getItemCount(): Int = items.size
}