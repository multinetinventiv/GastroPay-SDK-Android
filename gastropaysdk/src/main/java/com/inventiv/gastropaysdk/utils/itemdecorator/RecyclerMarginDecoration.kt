package com.inventiv.gastropaysdk.utils.itemdecorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class RecyclerMarginDecoration(
    private val margin: Int,
    private val columns: Int = 1
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        //set right margin to all
        outRect.right = margin
        //set bottom margin to all
        outRect.bottom = margin
        //we only add top margin to the first row
        if (position < columns) {
            outRect.top = margin
        }
        //add left margin only to the first column
        if (columns != 0) {
            if (position % columns == 0) {
                outRect.left = margin
            }
        } else { //for horizontal
            if (position == 0) {
                outRect.left = margin
            }
        }

    }
}