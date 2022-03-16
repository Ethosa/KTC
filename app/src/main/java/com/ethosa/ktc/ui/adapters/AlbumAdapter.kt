package com.ethosa.ktc.ui.adapters

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.LayoutAlbumImageBinding
import kotlin.math.abs


/**
 * Provides RecyclerView.Adapter behavior for album photos.
 */
class AlbumAdapter(
    private val items: List<String>,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private val dialog = Dialog(activity)
    private var animator = ObjectAnimator()
    private var animX = PropertyValuesHolder.ofFloat("x", 0f)
    private var animY = PropertyValuesHolder.ofFloat("y", 0f)
    private var img: ImageView? = null
    private var root: ConstraintLayout? = null
    private var pos: Int = -1

    companion object {
        private const val MAX_DIM_AMOUNT = 0.95f
        private const val HIDE_OFFSET = 400
        private const val CHANGE_IMAGE_OFFSET = 200
        private const val Y_PRIORITY = 3
        private const val DIM_STEP = 0.1f
    }

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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val image = items[position]
        // Download image
        Glide.with(binding.root)
            .load(image)
            .into(binding.imageView)

        // Show photo dialog.
        binding.root.setOnClickListener {
            showImage(image, position)
        }
    }

    private fun changeImage(inc: Int = 1) {
        pos += inc
        root!!.x = root!!.width.toFloat() * inc
        Glide.with(dialog.context)
            .load(items[pos])
            .into(img!!)
        animator = ObjectAnimator.ofPropertyValuesHolder(root!!, animX)
        animator.duration = 600
        animator.start()
    }

    private fun showImage(imgUrl: String, position: Int) {
        // Load image
        root!!.x = 0f
        root!!.y = 0f
        pos = position
        Glide.with(dialog.context)
            .load(imgUrl)
            .into(img!!)
        // Resize dialog
        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        dialog.window!!.attributes.dimAmount = MAX_DIM_AMOUNT
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(metrics.widthPixels, metrics.heightPixels)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dialog.window!!.attributes.blurBehindRadius = 5
        }
        dialog.show()
    }

    /**
     * @return image urls count
     */
    override fun getItemCount(): Int = items.size

    /**
     * Provides dialog behavior
     */
    @SuppressLint("ClickableViewAccessibility", "ObjectAnimatorBinding")
    private fun setupDialog() {
        // set content view
        dialog.window?.setContentView(R.layout.layout_album_photo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        root = dialog.findViewById(R.id.album_photo_root)
        img = dialog.findViewById(R.id.album_photo)
        var touchX = 0f
        var touchY = 0f
        var lastMotionEvent = 0

        // Provides touch
        root!!.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchY = motionEvent.y
                    touchX = motionEvent.x
                }
                // Move img on X
                MotionEvent.ACTION_MOVE -> {
                    val tmpX = motionEvent.x - touchX
                    val tmpY = motionEvent.y - touchY
                    if (lastMotionEvent == MotionEvent.ACTION_DOWN) {
                        if (abs(tmpX) > abs(tmpY)* Y_PRIORITY)
                            touchY = 0f
                        else
                            touchX = 0f
                    }
                    else if(touchY != 0f) {
                        root!!.y += tmpY
                        dialog.window!!.attributes.dimAmount -= abs(tmpY * DIM_STEP)
                    }
                    else if(touchX != 0f)
                        root!!.x += tmpX
                }
                // Hide and change image.
                MotionEvent.ACTION_UP -> {
                    if (lastMotionEvent == MotionEvent.ACTION_DOWN) {
                        dialog.dismiss()
                    } else if (root!!.y < -HIDE_OFFSET || root!!.y > root!!.width - HIDE_OFFSET) {
                        dialog.dismiss()
                    } else if (root!!.x < -CHANGE_IMAGE_OFFSET && pos < itemCount-1) {
                        changeImage()
                    } else if (root!!.x > CHANGE_IMAGE_OFFSET && pos > 0) {
                        changeImage(-1)
                    } else {
                        animator = ObjectAnimator.ofPropertyValuesHolder(root!!, animX, animY)
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