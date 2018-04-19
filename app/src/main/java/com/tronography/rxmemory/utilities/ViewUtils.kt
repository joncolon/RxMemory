package com.tronography.rxmemory.utilities

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.WindowManager


object ViewUtils {

    private var screenWidth = 0
    private var screenHeight = 0

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return Math.round(dp * density)
    }

    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }

        return screenHeight
    }

    fun getScreenWidth(c: Context): Int {
        if (screenWidth == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }

        return screenWidth
    }
}