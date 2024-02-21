package com.leadwoods.dissertation.support

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

internal const val AR_SUPPORTED = "AR_SUPPORTED"
internal const val AR_ENABLED = "AR_ENABLED"

internal const val GAME_MASTER = "GAME_MASTER"


open class Base: AppCompatActivity() {

    /**
     * Initializes default values in the shared preferences.
     */
    fun clearAllSettings() {
        Logger.d("called")
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPref.edit()
            .putBoolean(AR_SUPPORTED, false)
            .putBoolean(AR_ENABLED, false)
            .putBoolean(GAME_MASTER, false)
            .apply()
    }

}