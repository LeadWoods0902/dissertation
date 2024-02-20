package com.leadwoods.dissertation

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.leadwoods.dissertation.support.Logger

internal const val AR_SUPPORTED = "AR_SUPPORTED"
internal const val AR_ENABLED = "AR_ENABLED"


open class Base: AppCompatActivity() {

    /**
     * Initializes default values in the shared preferences.
     */
    fun init(){
        Logger.d("called")
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPref.edit()
            .putBoolean(AR_SUPPORTED, false)
            .putBoolean(AR_ENABLED, false)
            .apply()
    }

}