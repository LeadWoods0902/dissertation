package com.leadwoods.dissertation.fragments.ar_view

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.leadwoods.dissertation.support.preferences.PermisionHelpers

/**
 * Manages an ARCore Session using the Android Lifecycle API.
 *
 * Requests installation of ARCore services if not installed or not up to date
 *
 * Handles permission requesting
 */
class ARCoreHelper(
    val activity: Activity,
    val features: Set<Session.Feature> = setOf()
) : DefaultLifecycleObserver{
    var installRequested = false
    var session: Session? = null
        private set

    private var permissionHelper = PermisionHelpers()

    /**
     * In the event a session cannot be created, session remains null
     * Exception thrown
     */
    var exceptionCallback: ((Exception) -> Unit)? = null

    /**
     * Must be called before a Session is resumed
     *
     * Configure camera settings
     */
    var beforeSessionResumes: ((Session) -> Unit)? = null

    private fun tryCreateSession(): Session? {

        if (!permissionHelper.cameraHelper.hasCameraPermission(activity)) {
            permissionHelper.cameraHelper.requestCameraPermission(activity)
            return null
        }

        return try {
            when(ArCoreApk.getInstance().requestInstall(activity, !installRequested)) {
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    installRequested = true
                    return null
                }
                 ArCoreApk.InstallStatus.INSTALLED -> {}
            }

            Session(activity, features)
        } catch (err: Exception) {
            exceptionCallback?.invoke((err))
            null
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        val session = this.session ?: tryCreateSession() ?: return
        try {
            beforeSessionResumes?.invoke(session)
            session.resume()
            this.session = session
        } catch (err: CameraNotAvailableException) {
            exceptionCallback?.invoke(err)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        session?.pause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        session?.close()
        session = null
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, results: IntArray) {
        if(!permissionHelper.cameraHelper.hasCameraPermission(activity)){
            Toast.makeText(activity, "Camera permission is required to access this feature", Toast.LENGTH_LONG).show()

            if(!permissionHelper.cameraHelper.shouldShowRequestPermissionRationale(activity)){
                permissionHelper.launchPermissionSettings(activity)
            }
            activity.finish()
        }
    }
}