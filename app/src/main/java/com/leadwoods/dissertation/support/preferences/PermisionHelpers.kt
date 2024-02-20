package com.leadwoods.dissertation.support.preferences

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermisionHelpers {
    val cameraHelper = CameraPermissionHelper()
    val locationhelper = LocationPermissionHelper()

    /**
     * Launch Application settings to grant general permission
     */
    fun launchPermissionSettings(activity: Activity) {
        activity.startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", activity.packageName, null)
        })
    }
}

class CameraPermissionHelper {

    private val cameraPermissionCode = 0
    private val cameraPermissionName = Manifest.permission.CAMERA

    /**
     * Check if camera permissions have been granted
     */
    fun hasCameraPermission(activity: Activity?): Boolean {
        return (ContextCompat.checkSelfPermission(activity!!, cameraPermissionName)
                == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * Request camera permissions for this app
     */
    fun requestCameraPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(cameraPermissionName), cameraPermissionCode
        )
    }

    /**
     * Check whether app should show rationale for camera permission
     */
    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, cameraPermissionName)
    }
}

class LocationPermissionHelper {
    private val locationPermissionCode = 1

    private val coarseLocationPermissionName = Manifest.permission.ACCESS_COARSE_LOCATION
    private val fineLocationPermissionName = Manifest.permission.ACCESS_FINE_LOCATION

    fun hasFineLocationPermission(activity: Activity) : Boolean {
        return ContextCompat.checkSelfPermission(activity, fineLocationPermissionName) == PackageManager.PERMISSION_GRANTED
    }

    fun hasCoarseLocationPermission(activity: Activity) : Boolean {
        return ContextCompat.checkSelfPermission(activity, coarseLocationPermissionName) == PackageManager.PERMISSION_GRANTED
    }

    fun requestFineLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(fineLocationPermissionName), locationPermissionCode)
    }

    fun hasFineLocationPermissionsResponseInResult(permissions: Array<String>): Boolean {
        permissions.forEach {
            if(fineLocationPermissionName == it){
                return true
            }
        }

        return false
    }

    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, fineLocationPermissionName)
    }
}
