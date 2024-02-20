package com.leadwoods.dissertation.support

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.ar.core.ArCoreApk

fun Context.checkForARSupport() {
    Logger.d("called")

    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

    ArCoreApk.getInstance().checkAvailabilityAsync(this) { availability ->
        val isSupported = availability.isSupported
        sharedPref.edit().putBoolean("AR_SUPPORTED", isSupported).apply()
    }
}
fun Context.checkARPermissions(): Boolean {
    Logger.d("called")

    return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}

fun Context.requestARPermission(activity: Activity) {
    Logger.d("called")
    val builder = AlertDialog.Builder(activity)
    builder.apply {
        setMessage("Permission is required for AR functionality.") // Change placeholder with your message
        setPositiveButton("Okay") { dialog, _ ->
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
            dialog.dismiss()
        }
        setNegativeButton("Proceed without AR") { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
    }

    val dialog = builder.create()
    dialog.show()
}

fun Context.enableAR() {
    Logger.d("called")

    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    sharedPref.edit().putBoolean("AR_ENABLED", true).apply()

    Logger.d("ar has been enabled")

}

fun Context.disableAR() {
    Logger.d("called")

    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    sharedPref.edit().putBoolean("AR_ENABLED", false).apply()

    Logger.d("ar has been disabled")

}
