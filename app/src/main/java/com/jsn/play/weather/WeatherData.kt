package com.jsn.play.weather

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


data class Weather(
    var results: List<Result>
) {
    data class Result(
        var last_update: String,
        var location: Location,
        var now: Now
    ) {
        data class Location(
            var country: String,
            var id: String,
            var name: String,
            var path: String,
            var timezone: String,
            var timezone_offset: String
        )

        data class Now(
            var clouds: String,
            var code: String,
            var dew_point: String,
            var feels_like: String,
            var humidity: String,
            var pressure: String,
            var temperature: String,
            var text: String,
            var visibility: String,
            var wind_direction: String,
            var wind_direction_degree: String,
            var wind_scale: String,
            var wind_speed: String
        )
    }
}

interface WeatherApi {
    @GET("weather/now.json")
    suspend fun getWeather(@Query("key")key:String?="Sm_q8bqKdJEIwcnaU",
                           @Query("location") location:String?="hangzhou",
                           @Query("language") language:String?="zh-Hans",
                           @Query("unit") unit: String?="c"

    ) :Weather
}


const val url="https://api.seniverse.com/v3/"

/*?key=Sm_q8bqKdJEIwcnaU&location=hangzhou&language=&unit=c*/

val retrofit by lazy {
    Retrofit.Builder().run {
        baseUrl(url)
        addConverterFactory(MoshiConverterFactory.create().asLenient()) //宽容
        build()
    }
}

val weatherService by lazy{
    retrofit.create(WeatherApi::class.java)
}