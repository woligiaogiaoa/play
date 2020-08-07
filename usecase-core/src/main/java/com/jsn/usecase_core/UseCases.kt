package com.jsn.usecase_core

import android.view.animation.AnticipateInterpolator
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*

interface ObservableUseCase<T>{

    val dispatcher:CoroutineDispatcher

    fun observe():Flow<T>

}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
abstract class FlowUseCase<T:Any,params:Any>:ObservableUseCase<T>{

    private val channel=ConflatedBroadcastChannel<params>()

    protected abstract fun doWork(params: params):Flow<T>

    operator fun invoke(params: params) =channel.sendBlocking(params)

    fun produce(params: params)=doWork(params).flowOn(dispatcher)

    override fun observe(): Flow<T> = channel.asFlow()
        .distinctUntilChanged()
        .flatMapConcat { params ->
            doWork(params)
        }
        .flowOn(dispatcher)
}

abstract class NoResultUseCase<in T>{

    protected abstract val dispatcher:CoroutineDispatcher

    suspend operator fun invoke(params:T)= withContext(dispatcher){
        run(params)
    }

    abstract suspend fun run(params: T)
}