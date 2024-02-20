package com.leadwoods.dissertation.support.preferences

import android.content.Context
import android.content.SharedPreferences

class InstantPlacementSettings(context: Context) {
    val SHARED_PREFERENCES_ID = "SHARED_PREFERENCES_INSTANT_PLACEMENT_OPTIONS"
    val SHARED_PREFERENCES_INSTANT_PLACEMENT_ENABLED = "instant_placement_enabled"
    private var instantPlacementEnabled = false
    private var sharedPreferences: SharedPreferences


    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE)
        instantPlacementEnabled = sharedPreferences.getBoolean(SHARED_PREFERENCES_INSTANT_PLACEMENT_ENABLED, false)
    }

    fun isInstantPlacementEnabled(): Boolean {
        return instantPlacementEnabled
    }

    fun setInstantPlacementEnabled(enabled: Boolean) {
        if(enabled == instantPlacementEnabled)
            return

        instantPlacementEnabled = enabled
        sharedPreferences.apply {
            edit().putBoolean(SHARED_PREFERENCES_INSTANT_PLACEMENT_ENABLED, instantPlacementEnabled).apply()
        }
    }
}