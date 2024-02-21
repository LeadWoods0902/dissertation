package com.leadwoods.dissertation.fragments.ar_view

import com.google.ar.core.Anchor
import com.google.ar.core.Trackable
import com.leadwoods.dissertation.activities.main_switcher.MainActivity

private data class WrappedAnchor(
    val anchor: Anchor,
    val trackable: Trackable,
)

class ARRenderer(private val activity: MainActivity){}