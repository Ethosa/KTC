package com.ethosa.ktc.ui.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacingItemDecoration(private val spaceH: Int, private val spaceV: Int = spaceH*2) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = spaceH
        outRect.right = spaceH
        outRect.bottom = spaceV

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0)
            outRect.top = spaceV
        else
            outRect.top = 0
    }
}