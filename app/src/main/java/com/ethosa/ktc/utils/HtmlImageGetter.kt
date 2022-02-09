package com.ethosa.ktc.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

/**
 * Provides image fetching from HTML text
 */
class HtmlImageGetter(
    private val res: Resources,
    private val textView: TextView
) : Html.ImageGetter {
    /**
     * Fetches images from HTML text
     */
    @OptIn(DelicateCoroutinesApi::class)
    override fun getDrawable(p0: String?): Drawable {
        val holder = PlaceHolder(res, null)
        GlobalScope.launch(Dispatchers.IO) {
            run {
                // Download image and converts to Drawable
                val bitmap = Picasso.with(textView.context).load(p0!!).get()
                val drawable = BitmapDrawable(res, bitmap)
                // Scale image with keep aspect ratio
                val width = res.displayMetrics.widthPixels
                val aspectRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()
                val height = (width / aspectRatio).toInt()
                // Change bounds
                drawable.setBounds(0, 0, width, height)
                holder.setDrawable(drawable)
                holder.setBounds(0, 0, width, height)
                // Update text
                withContext(Dispatchers.Main) {
                    textView.text = textView.text
                }
            }
        }
        return holder
    }

    internal class PlaceHolder(res: Resources, bitmap: Bitmap?) : BitmapDrawable(res, bitmap){
        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }

        fun setDrawable(d: Drawable) {
            drawable = d
        }
    }
}