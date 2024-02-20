package com.leadwoods.dissertation.ui.player_overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerOverviewViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the \"your character\" fragment"
    }
    val text: LiveData<String> = _text
}