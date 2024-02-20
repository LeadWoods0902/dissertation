package com.leadwoods.dissertation.ui.turn_tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TurnTrackerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the \"turn tracker\" fragment"
    }
    val text: LiveData<String> = _text
}