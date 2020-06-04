package com.jsn.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow


sealed class ScrollDirection(){
    object SCROLL_NON_DIR:ScrollDirection()
    object SCROLL_UP:ScrollDirection()
    object SCROLL_DOWN:ScrollDirection()
}


class MainViewModle :ViewModel() {


    val scrollChannel=ConflatedBroadcastChannel<ScrollDirection>()

    val ScrollStateFlow=scrollChannel.asFlow()

}