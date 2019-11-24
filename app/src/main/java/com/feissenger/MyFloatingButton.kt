package com.feissenger

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class MyFloatingButton : FloatingActionButton, View.OnTouchListener {

    private var downRawX: Float = 0.toFloat()
    private var downRawY: Float = 0.toFloat()
    private var dX: Float = 0.toFloat()
    private var dY: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams

        val action = motionEvent.action
        if (action == MotionEvent.ACTION_DOWN) {

            downRawX = motionEvent.rawX
            downRawY = motionEvent.rawY
            dX = view.x - downRawX
            dY = view.y - downRawY

            return true // Consumed

        } else if (action == MotionEvent.ACTION_MOVE) {

            val viewHeight = view.height

            val viewParent = view.parent as View
            val parentHeight = viewParent.height

            val margin = view.marginBottom

            var newY = motionEvent.rawY + dY
            newY = max(
                layoutParams.topMargin.toFloat()+margin,
                newY
            ) // Don't allow the FAB past the top of the parent
            newY = min(
                (parentHeight - viewHeight - layoutParams.bottomMargin).toFloat()-margin,
                newY
            ) // Don't allow the FAB past the bottom of the parent

            view.animate()
                .y(newY)
                .setDuration(0)
                .start()

            return true // Consumed

        } else if (action == MotionEvent.ACTION_UP) {

            val upRawX = motionEvent.rawX
            val upRawY = motionEvent.rawY

            val upDX = upRawX - downRawX
            val upDY = upRawY - downRawY

            return if (abs(upDX) < CLICK_DRAG_TOLERANCE && abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                performClick()
            } else { // A drag
                true // Consumed
            }

        } else {
            return super.onTouchEvent(motionEvent)
        }

    }

    companion object {

        private val CLICK_DRAG_TOLERANCE =
            10f // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    }

}