package com.leadwoods.dissertation.support.preferences

import android.content.Context
import android.content.SharedPreferences

class DepthSettings(context: Context) {

    val SHARED_PREFERENCES_ID = "SHARED_PREFERENCES_OCCLUSION_OPTIONS"
    val SHARED_PREFERENCES_SHOW_DEPTH_ENABLE_DIALOG_OOBE = "show_depth_enable_dialog_oobe"
    val SHARED_PREFERENCES_USE_DEPTH_FOR_OCCLUSION = "use_depth_for_occlusion"

    var depthColorVisualizationEnabled = false
    var useDepthForOcclusion = false

    private var sharedPreferences: SharedPreferences

    /**
     * Configures settings for current session using
     */
    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_ID,  Context.MODE_PRIVATE)

        useDepthForOcclusion = sharedPreferences.getBoolean(SHARED_PREFERENCES_USE_DEPTH_FOR_OCCLUSION, false)
    }

    fun useDepthForOcclusion(): Boolean{
        return useDepthForOcclusion
    }

    fun setUseDepthForOcclusion(enable: Boolean) {
        if(enable == useDepthForOcclusion)
            return

        useDepthForOcclusion = enable
        sharedPreferences.apply{
            edit().putBoolean(SHARED_PREFERENCES_USE_DEPTH_FOR_OCCLUSION, useDepthForOcclusion).apply()
        }
    }
}