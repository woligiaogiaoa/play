package com.jsn.play.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsn.play.data.Result
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel:ViewModel() {

    val weatherApi by lazy {
        weatherService
    }

    val channel=ConflatedBroadcastChannel<Result<Weather>>()

    val weatheFlow=channel
        .asFlow()

    init {
        viewModelScope.launch {
            var errorCount =0
            while (true){
                channel.offer(
                    try {
                        Result.Success(weatherApi.getWeather())
                    }catch (e:Exception){
                        errorCount++
                        Result.Error(e)
                    }

                )
                if(errorCount>=10){ //stop streaming
                    break
                }
                delay(4_000)
                try {
                    channel.offer(
                        Result.Loading
                    )
                }finally {

                }

            }
        }
    }
    suspend fun produce(){

    }

    override fun onCleared() {
        super.onCleared()
        channel.cancel()
    }

}