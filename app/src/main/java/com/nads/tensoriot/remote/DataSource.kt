package com.nads.tensoriot.remote

import com.nads.tensoriot.model.Weathers
import com.nads.tensoriot.model.sevenday.SevenDayForeCast
import retrofit2.http.GET
import retrofit2.http.Query

interface DataSource {
    @GET("weather")
    suspend fun getWeathers(@Query("q")city:String, @Query("APPID")appId:String):Weathers

    @GET("forecast")
    suspend fun getWeathersSeven(@Query("q")city:String,
                                 @Query("cnt")count:String,
                                 @Query("APPID")appId:String):SevenDayForeCast
}