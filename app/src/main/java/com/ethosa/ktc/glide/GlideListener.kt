package com.ethosa.ktc.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * Provides RequestListener behavior for Bitmap
 */
class GlideListener(
    private val callback: GlideCallback
) : RequestListener<Bitmap> {
    /**
     * Called when resource successfully loaded.
     */
    override fun onResourceReady(
        resource: Bitmap?,
        model: Any?,
        target: Target<Bitmap>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        callback.onReady(resource!!)
        return true
    }

    /**
     * Called when resource failure loaded
     */
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Bitmap>?,
        isFirstResource: Boolean
    ): Boolean {
        callback.onFailure(e!!)
        return true
    }
}