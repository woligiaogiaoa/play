package com.jsn.play.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jsn.play.util.SingleLiveEvent

class UnsplashViewModel :ViewModel() {
    val position=SingleLiveEvent<Int>().apply { value=0 }
}