package com.ethosa.ktc.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.ethosa.ktc.R
import com.ethosa.ktc.ui.adapters.AlbumAdapter
import kotlin.math.abs

/**
 * Provides photo dialog.
 */
@SuppressLint("ClickableViewAccessibility")
class AlbumPhotoDialog(
    private val adapter: AlbumAdapter
) : View.OnTouchListener {
    private var animator = ObjectAnimator()
    private var animX = PropertyValuesHolder.ofFloat("x", 0f)
    private var animY = PropertyValuesHolder.ofFloat("y", 0f)
    private var root: ConstraintLayout? = null
    private val dialog = Dialog(adapter.activity)
    private var img: ImageView? = null
    private var pos: Int = -1
    private var percentHeight: Float = 1f

    private var touchX = 0f
    private var touchY = 0f
    private var lastMotionEvent = 0

    companion object {
        private const val MAX_DIM_AMOUNT = 0.95f
        private const val HIDE_OFFSET = 400
        private const val CHANGE_IMAGE_OFFSET = 200
        private const val Y_PRIORITY = 2
    }

    /**
     * Setups dialog.
     */
    init {
        // set content view
        dialog.window?.setContentView(R.layout.layout_album_photo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        img = dialog.findViewById(R.id.album_photo)

        // Provides touch
        root = dialog.findViewById(R.id.album_photo_root)
        root!!.setOnTouchListener(this)
    }

    /**
     * Shows this dialog.
     */
    fun showImage(imgUrl: String, position: Int) {
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
        adapter.activity.windowManager.defaultDisplay.getMetrics(metrics)
        dialog.window!!.attributes.dimAmount = MAX_DIM_AMOUNT
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        dialog.window?.setLayout(metrics.widthPixels, metrics.heightPixels)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dialog.window!!.attributes.blurBehindRadius = 5
        }
        dialog.show()
    }

    private fun changeImage(inc: Int = 1) {
        pos += inc
        root!!.x = root!!.width.toFloat() * inc
        Glide.with(dialog.context)
            .load(adapter.items[pos])
            .into(img!!)
        animator = ObjectAnimator.ofPropertyValuesHolder(root!!, animX)
        animator.duration = 600
        animator.start()
    }

    /**
     * Provides horizontal and vertical swipes
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> onTouchDown(motionEvent)
            MotionEvent.ACTION_MOVE -> onTouchMove(motionEvent)
            MotionEvent.ACTION_UP -> onTouchUp()
        }
        lastMotionEvent = motionEvent.action
        return true
    }

    /**
     * Calls when photo just touched.
     */
    private fun onTouchDown(motionEvent: MotionEvent) {
        touchY = motionEvent.y
        touchX = motionEvent.x
    }

    /**
     * Calls when moves touch
     */
    private fun onTouchMove(motionEvent: MotionEvent) {
        // calculate tmp positions
        val tmpX = motionEvent.x - touchX
        val tmpY = motionEvent.y - touchY
        // if user just tapped
        if (lastMotionEvent == MotionEvent.ACTION_DOWN) {
            if (abs(tmpX) > abs(tmpY) * Y_PRIORITY)
                // user swipes right or left
                touchY = 0f
            else
                // user swipes up or down
                touchX = 0f
        }
        else if(touchY != 0f) {
            root!!.y += tmpY
            percentHeight = root!!.height / 100f
            dialog.window!!.setDimAmount(
                MAX_DIM_AMOUNT - (abs(root!!.y / percentHeight) * 0.01f)
            )
        }
        else if(touchX != 0f)
            root!!.x += tmpX
    }

    /**
     * Calls when canceled touch
     */
    private fun onTouchUp() {
        if (lastMotionEvent == MotionEvent.ACTION_DOWN) {
            // last event is just touch
            dialog.dismiss()
        } else if (root!!.y < -HIDE_OFFSET || root!!.y > root!!.width - HIDE_OFFSET) {
            // user swipes image up or down
            dialog.dismiss()
        } else if (root!!.x < -CHANGE_IMAGE_OFFSET && pos < adapter.itemCount-1) {
            // user swipes image left
            changeImage()
        } else if (root!!.x > CHANGE_IMAGE_OFFSET && pos > 0) {
            // user swipes image right
            changeImage(-1)
        } else {
            dialog.window!!.setDimAmount(MAX_DIM_AMOUNT)
            animator = ObjectAnimator.ofPropertyValuesHolder(root!!, animX, animY)
            animator.duration = 600
            animator.start()
        }
    }
}