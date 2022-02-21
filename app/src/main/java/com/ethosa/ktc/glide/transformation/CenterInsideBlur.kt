package com.ethosa.ktc.glide.transformation

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import jp.wasabeef.glide.transformations.internal.FastBlur
import java.security.MessageDigest

/**
 * Provides transformation with centered image and blurred it.
 */
class CenterInsideBlur @JvmOverloads constructor(
    private val radius: Int = MAX_RADIUS,
    private val sampling: Int = DEFAULT_DOWN_SAMPLING
) : BitmapTransformation() {

    companion object {
        private const val VERSION = 1
        const val ID = "com.ethosa.ktc.utils.CenterInsideBlurTransformation.$VERSION"

        const val MAX_RADIUS = 25
        const val DEFAULT_DOWN_SAMPLING = 1
    }

    override fun transform(
        context: Context, pool: BitmapPool,
        toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap {
        val blur = blurBitmap(pool, toTransform)
        val centerCrop = TransformationUtils.centerCrop(pool, blur, outWidth, outHeight)
        val centerInside = TransformationUtils.centerInside(pool, toTransform, outWidth, outHeight)
        return mergeBitmap(centerCrop, centerInside)
    }

    override fun toString(): String {
        return "CenterInsideBlurTransformation(radius=$radius, sampling=$sampling)"
    }

    override fun equals(other: Any?): Boolean {
        return other is CenterInsideBlur
                && other.radius == radius
                && other.sampling == sampling
    }

    override fun hashCode(): Int {
        return ID.hashCode() + radius * 1000 + sampling * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + radius + sampling).toByteArray(Key.CHARSET))
    }

    /**
     * Blurs bitmap.
     * @param src source bitmap
     */
    private fun blurBitmap(pool: BitmapPool, src: Bitmap): Bitmap {
        val width = src.width
        val height = src.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling
        var blurred: Bitmap = pool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]
        setCanvasBitmapDensity(src, blurred)
        val canvas = Canvas(blurred)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(src, 0f, 0f, paint)
        blurred = FastBlur.blur(blurred, radius, true)

        return blurred
    }

    /**
     * Merges two bitmaps into one.
     * @param bg background bitmap
     * @param fg foreground bitmap
     */
    private fun mergeBitmap(bg: Bitmap, fg: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(
            bg.width, bg.height,
            bg.config
        )
        val canvas = Canvas(result)
        canvas.drawBitmap(bg, 0f, 0f, null)
        val m = Matrix()
        m.setRectToRect(
            RectF(0f, 0f, fg.width.toFloat(), fg.height.toFloat()),
            RectF(0f, 0f, bg.width.toFloat(), bg.height.toFloat()),
            Matrix.ScaleToFit.CENTER
        )
        val scaled = Bitmap.createBitmap(
            fg, 0, 0,
            fg.width, fg.height, m, true
        )
        if (scaled.width < bg.width) {
            canvas.drawBitmap(
                scaled, (bg.width - scaled.width) / 2f,
                0f, null
            )
        } else {
            canvas.drawBitmap(
                scaled, 0f,
                (bg.height - scaled.height) / 2f, null
            )
        }
        return result
    }
}