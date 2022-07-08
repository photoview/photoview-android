package com.github.photoview.photoview_android.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlacesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is places Fragment"
    }
    val text: LiveData<String> = _text
}