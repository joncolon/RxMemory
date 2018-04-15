package com.tronography.rxmemory.utilities

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View
import javax.inject.Inject

/**
 * @description: Custom RecyclerView.ItemDecoration used to create even spacing between items in a
 * RecyclerView GridLayout. A RecyclerView using this ItemDecoration should declare half of
 * of the desired spacing as Padding in the RecyclerView's XML.
 */
class GridItemOffsetDecorator
@Inject constructor(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}