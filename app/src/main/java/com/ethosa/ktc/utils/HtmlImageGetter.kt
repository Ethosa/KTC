package com.ethosa.ktc.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import androidx.annotation.Keep
import kotlinx.coroutines.*
import java.net.URL


/**
 * Provides image fetching from HTML text
 */
@Keep
@Suppress("OPT_IN_IS_NOT_ENABLED")
class HtmlImageGetter(
    private val res: Resources,
    private val textView: TextView
) : Html.ImageGetter {
    /**
     * Fetches images from HTML text
     */
    @OptIn(DelicateCoroutinesApi::class)
    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    override fun getDrawable(p0: String?): Drawable {
        val holder = PlaceHolder(res, null)
        GlobalScope.launch(Dispatchers.IO) {
            // Download image and converts to Drawable
            try {
                val inputStream = URL(p0).openConnection().getInputStream()
                val drawable = Drawable.createFromStream(inputStream, "src")
                inputStream.close()

                // Scale image with keep aspect ratio
                val width = res.displayMetrics.widthPixels
                val aspectRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight
                val height = (width / aspectRatio).toInt()

                // Change bounds
                drawable.setBounds(0, 0, width, height)
                holder.drawable = drawable
                holder.setBounds(0, 0, width, height)
                // Update text
                withContext(Dispatchers.Main) {
                    textView.text = textView.text
                }
            } catch (e: Exception) { }
        }
        return holder
    }

    /**
     * Drawable placeholder
     */
    class PlaceHolder(
        res: Resources,
        bitmap: Bitmap?
    ) : BitmapDrawable(res, bitmap) {
        var drawable: Drawable? = null
        /**
         * Draws canvas on drawable.
         */
        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }
    }
}