package com.tronography.rxmemory.ui.layoutmanagers

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup

class SpanningGridLayoutManager : GridLayoutManager {

    private val horizontalSpace: Int
        get() = width - paddingRight - paddingLeft

    private val verticalSpace: Int
        get() = height - paddingBottom - paddingTop

    constructor(
            context: Context,
            attrs: AttributeSet,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {}

    constructor(
            context: Context,
            spanCount: Int
    ) : super(context, spanCount) {}

    constructor(
            context: Context,
            spanCount: Int,
            orientation: Int,
            reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout) {}

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(c: Context, attrs: AttributeSet): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(c, attrs))
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(lp))
    }

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            layoutParams.width = Math.round(horizontalSpace / Math.ceil((itemCount / spanCount).toDouble())).toInt()
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            layoutParams.height = Math.round(verticalSpace / Math.ceil((itemCount / spanCount).toDouble())).toInt()
        }
        return layoutParams
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}