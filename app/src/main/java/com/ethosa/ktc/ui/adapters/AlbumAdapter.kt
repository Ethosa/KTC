package com.ethosa.ktc.ui.adapters

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.AlbumImageBinding

class AlbumAdapter(
    private val items: List<String>,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private val dialog = Dialog(activity)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = AlbumImageBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_image, parent, false)
        dialog.window?.setContentView(R.layout.album_photo)
        val root = dialog.findViewById<ConstraintLayout>(R.id.album_photo_root)
        root.setOnClickListener {
            dialog.dismiss()
        }
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val binding = holder.binding
        val image = items[pos]
        // Download image
        Glide.with(binding.root)
            .load(image)
            .into(binding.imageView)

        binding.root.setOnClickListener {
            val img = dialog.findViewById<ImageView>(R.id.album_photo)
            // Load image
            Glide.with(dialog.context)
                .load(image)
                .into(img!!)
            // Resize dialog
            val metrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            dialog.window?.setLayout(metrics.widthPixels, metrics.heightPixels)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    override fun getItemCount(): Int = items.size
}