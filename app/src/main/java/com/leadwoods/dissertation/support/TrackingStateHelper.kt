package com.leadwoods.dissertation.support

import android.app.Activity
import android.view.WindowManager
import com.google.ar.core.Camera
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState

class TrackingStateHelper(val activity: Activity) {

    private val INSUFFICIENT_FEATURES_MESSAGE = "Can't find anything. Aim device at a surface with more texture or color."
    private val EXCESSIVE_MOTION_MESSAGE = "Moving too fast. Slow down."
    private val INSUFFICIENT_LIGHT_MESSAGE = "Too dark. Try moving to a well-lit area."
    private val INSUFFICIENT_LIGHT_ANDROID_S_MESSAGE = ("Too dark. Try moving to a well-lit area. Also, make sure 'Block Camera' is set to off in system settings.")
    private val BAD_STATE_MESSAGE = "Tracking lost due to bad internal state. Please try restarting the AR experience."
    private val CAMERA_UNAVAILABLE_MESSAGE = "Another app is using the camera. Tap on this app or try closing the other one."

    private lateinit var previousTrackingState: TrackingState

    fun updateKeepScreenOnFlag(trackingState: TrackingState) {
        if(trackingState == previousTrackingState)
            return

        previousTrackingState = trackingState
        when(trackingState){
            TrackingState.PAUSED, TrackingState.STOPPED -> {
                activity.runOnUiThread { activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
            }
            TrackingState.TRACKING -> {
                activity.runOnUiThread { activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
            }
        }
    }

    fun getTrackingFailureReasonString(camera: Camera): String {
        return when(val reason = camera.trackingFailureReason){
            TrackingFailureReason.NONE -> {""}
            TrackingFailureReason.BAD_STATE -> {BAD_STATE_MESSAGE}
            TrackingFailureReason.INSUFFICIENT_LIGHT -> {INSUFFICIENT_LIGHT_ANDROID_S_MESSAGE}
            TrackingFailureReason.EXCESSIVE_MOTION -> {EXCESSIVE_MOTION_MESSAGE}
            TrackingFailureReason.INSUFFICIENT_FEATURES -> {INSUFFICIENT_FEATURES_MESSAGE}
            TrackingFailureReason.CAMERA_UNAVAILABLE -> {CAMERA_UNAVAILABLE_MESSAGE}
            else -> {"Unknown Tracking Failure Reason: $reason"}
        }
    }
}