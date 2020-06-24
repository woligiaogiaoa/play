package com.jsn.play.motion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MotionViewModel :ViewModel(){

    val text=MutableLiveData<String>()

    val listData=MutableLiveData<MutableList<String>>().apply {
        value=data
    }

    val data= mutableListOf<String>(
        "1","1","1"
        ,"1","1"
        ,"1","1"
        ,"1","1","" + "1","1"
        ,"1","1","1", "1","1","1","1" + "","1","1", "1","1","1")
}