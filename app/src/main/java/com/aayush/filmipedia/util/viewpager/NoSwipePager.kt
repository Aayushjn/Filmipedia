package com.aayush.filmipedia.util.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

class NoSwipePager(context: Context, attrs: AttributeSet): ViewPager(context, attrs) {
    var isSwipeEnabled = true

    override fun onTouchEvent(ev: MotionEvent) =
        if (isSwipeEnabled) {
            performClick()
            super.onTouchEvent(ev)
        } else false

    override fun performClick() = super.performClick()

    override fun onInterceptTouchEvent(ev: MotionEvent) =
        if (isSwipeEnabled) {
            super.onInterceptTouchEvent(ev)
        } else false
}