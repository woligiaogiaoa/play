package com.jsn.play.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow


sealed class ScrollDirection(){
    object SCROLL_NON_DIR: ScrollDirection()
    object SCROLL_UP: ScrollDirection()
    object SCROLL_DOWN: ScrollDirection()
}


class MainViewModle :ViewModel() {


    val scrollChannel=ConflatedBroadcastChannel<ScrollDirection>()

    val ScrollStateFlow=scrollChannel.asFlow()


    override fun onCleared() {
        scrollChannel.close()
    }

}