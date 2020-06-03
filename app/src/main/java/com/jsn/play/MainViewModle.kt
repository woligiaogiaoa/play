package com.jsn.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModle :ViewModel() {
    val scrollDir=MutableLiveData<Int>()
}