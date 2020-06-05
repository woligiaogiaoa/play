package com.jsn.play.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest


sealed class ScrollDirection(){
    object SCROLL_NON_DIR: ScrollDirection()
    object SCROLL_UP: ScrollDirection()
    object SCROLL_DOWN: ScrollDirection()
}


class MainViewModle :ViewModel() {


    val scrollChannel=ConflatedBroadcastChannel<ScrollDirection>()

    val ScrollStateFlow=scrollChannel
        .asFlow()
        .distinctUntilChanged()


    override fun onCleared() {
        scrollChannel.close()
    }

}