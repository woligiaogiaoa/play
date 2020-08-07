package com.jsn.play.home

import androidx.lifecycle.ViewModel
import com.jsn.play.AnimationState
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged


sealed class ScrollDirection(){
    object SCROLL_NON_DIR: ScrollDirection()
    object SCROLL_UP: ScrollDirection()
    object SCROLL_DOWN: ScrollDirection()
}


class MainViewModle :ViewModel() {


    val scrollChannel=ConflatedBroadcastChannel<ScrollDirection>()

    val animationChannel=ConflatedBroadcastChannel<AnimationState>()

    val ScrollStateFlow=scrollChannel
        .asFlow()
        .distinctUntilChanged()

    val animationFlow=
        animationChannel.asFlow()
            .distinctUntilChanged()



    override fun onCleared() {
        scrollChannel.close()
        animationChannel.close()
    }

}