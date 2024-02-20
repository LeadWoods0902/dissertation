package com.leadwoods.dissertation.support

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.display.DisplayManager.DisplayListener
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import com.google.ar.core.Session
import kotlin.properties.Delegates

class DisplayRotationHelper(
    context: Context,
    val displayManager: DisplayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager,
    val cameraManager: CameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager,
    val display: Display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
): DisplayListener {

    private var viewportChanged by Delegates.notNull<Boolean>()
    private var viewportWidth by Delegates.notNull<Int>()
    private var viewportHeight by Delegates.notNull<Int>()

    fun onResume() {
        displayManager.registerDisplayListener(this, null)
    }

    fun onPause() {
        displayManager.unregisterDisplayListener(this)
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        viewportWidth = width
        viewportHeight = height
        viewportChanged = true
    }

    fun updateSessionIfNeeded(session: Session) {
        if(viewportChanged) {
            val displayRotation: Int = display.rotation
            session.setDisplayGeometry(displayRotation, viewportWidth, viewportHeight)
            viewportChanged = false
        }
    }

    fun getCameraSensorRelativeViewportAspectRatio(cameraID: String): Float {
        return when(val cameraSensorToDisplayRotation = getCameraSensorToDisplayRotation(cameraID)) {
            90, 270 -> {
                viewportHeight.toFloat() / viewportWidth
            }
            0, 180 -> {
                viewportWidth.toFloat() / viewportHeight
            }
            else -> {
                throw RuntimeException("Unhandle-able device rotation: $cameraSensorToDisplayRotation")
            }
        }
    }

    fun getCameraSensorToDisplayRotation(cameraID: String): Int{
        val characteristics: CameraCharacteristics
        try{
            characteristics = cameraManager.getCameraCharacteristics(cameraID)
        } catch (err: CameraAccessException){
            throw RuntimeException("Unable to determine display orientation: $err")
        }

        val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)?.toInt() ?: 0
        val displayOrientation = toDegrees(display.rotation)

        return (sensorOrientation + displayOrientation + 360) % 360
    }

    private fun toDegrees(rotation: Int): Int {
        return when (rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> throw RuntimeException("Unknown rotation $rotation")
        }
    }



    override fun onDisplayAdded(displayId: Int) {}

    override fun onDisplayRemoved(displayId: Int) {}

    override fun onDisplayChanged(displayId: Int) {
        viewportChanged = true
    }
}

