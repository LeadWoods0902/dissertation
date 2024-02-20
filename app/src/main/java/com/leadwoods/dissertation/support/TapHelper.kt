package com.leadwoods.dissertation.support

import android.content.Context
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.view.GestureDetectorCompat
import java.util.concurrent.BlockingQueue

class TapHelper(context: Context): OnTouchListener {

    private lateinit var gestureDetector: GestureDetectorCompat

    private lateinit var queuedSingleTaps: BlockingQueue<MotionEvent>

    init {
        gestureDetector = GestureDetectorCompat(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                queuedSingleTaps.offer(e)
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
        })
    }

    fun poll(): MotionEvent? {
        return queuedSingleTaps.poll()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if(event == null)
            return false

        return gestureDetector.onTouchEvent(event)
    }
}