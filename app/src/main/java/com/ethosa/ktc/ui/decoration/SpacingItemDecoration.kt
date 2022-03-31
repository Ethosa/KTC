package com.ethosa.ktc.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Provides spacing item decoration for RecyclerView.
 */
class SpacingItemDecoration(
    private val spaceH: Int,
    private val spaceV: Int = spaceH*2
) : ItemDecoration() {
    /**
     * Calculates item offsets
     */
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = spaceH
        outRect.right = spaceH
        outRect.bottom = spaceV

        // Add top margin only for the first item to avoid double space between items
        val layoutPos = parent.getChildLayoutPosition(view)
        outRect.top = if (layoutPos == 0) spaceV else 0
    }
}