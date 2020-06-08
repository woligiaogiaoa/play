package com.jsn.play.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsn.play.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel:ViewModel() {

    val weatherApi by lazy {
        weatherService
    }

    val channel=ConflatedBroadcastChannel<Result<Weather>>()

    val weatheFlow=channel
        .asFlow()

    fun fetchWeather() = flow<Result<Weather>> {
        emit(Result.Loading)
        emit( try {
            Result.Success(weatherApi.getWeather())
        }catch (e:Exception){
            Result.Error(e)
        })
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)


    init {
        //streaming data
        viewModelScope.launch {
            var interval=4000L
            var errorCount =0

            while (true){
                channel.offer(Result.Loading)
                channel.offer(
                    try {
                        Result.Success(weatherApi.getWeather())
                    }catch (e:Exception){
                        errorCount++
                        Result.Error(e)
                    }

                )
                if(errorCount>=10){ //stop streaming
                    interval=5000
                }
                delay(interval)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        channel.close()
    }

}