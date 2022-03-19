package com.ethosa.ktc.ui.adapters

import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.LayoutAlbumImageBinding
import com.ethosa.ktc.utils.AlbumPhotoDialog


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
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_album_image, parent, false)
        return ViewHolder(inflater)
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