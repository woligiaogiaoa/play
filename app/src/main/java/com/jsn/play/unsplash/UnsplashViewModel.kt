package com.jsn.play.unsplash

import androidx.lifecycle.ViewModel
import com.jsn.play.util.SingleLiveEvent

class UnsplashViewModel :ViewModel() {
    val position=SingleLiveEvent<Int>().apply { value=0 }
}