package com.ethosa.ktc.ui.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.LayoutAlbumImageBinding

/**
 * Provides RecyclerView.Adapter behavior for album photos.
 */
class AlbumAdapter(
    private val items: List<String>,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private val dialog = Dialog(activity)
    private var animator = ObjectAnimator()
    private var img: ImageView? = null
    private var root: ConstraintLayout? = null

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
        setupDialog()
        return ViewHolder(inflater)
    }

    /**
     * Builds photo
     */
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val binding = holder.binding
        val image = items[pos]
        // Download image
        Glide.with(binding.root)
            .load(image)
            .into(binding.imageView)

        // Show photo dialog.
        binding.root.setOnClickListener {
            // Load image
            root!!.x = 0f
            root!!.y = 0f
            Glide.with(dialog.context)
                .load(image)
                .into(img!!)
            // Resize dialog
            val metrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            dialog.window?.setLayout(metrics.widthPixels, metrics.heightPixels)
            dialog.show()
        }
    }

    /**
     * @return image urls count
     */
    override fun getItemCount(): Int = items.size

    /**
     * Provides dialog behavior
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupDialog() {
        // set content view
        dialog.window?.setContentView(R.layout.layout_album_photo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        root = dialog.findViewById(R.id.album_photo_root)
        img = dialog.findViewById(R.id.album_photo)
        var touchY = 0f
        var lastMotionEvent = 0
        val hideHeight = 400

        // Provides touch
        root!!.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchY = motionEvent.y
                }
                MotionEvent.ACTION_MOVE -> {
                    root!!.y += motionEvent.y - touchY
                }
                MotionEvent.ACTION_UP -> {
                    if (lastMotionEvent == MotionEvent.ACTION_DOWN) {
                        dialog.dismiss()
                    } else if (root!!.y < -hideHeight || root!!.y > root!!.width - hideHeight) {
                        dialog.dismiss()
                    } else {
                        animator = ObjectAnimator.ofFloat(root!!, "y", 0f)
                        animator.duration = 600
                        animator.start()
                    }
                }
            }
            lastMotionEvent = motionEvent.action
            return@setOnTouchListener true
        }
    }
}