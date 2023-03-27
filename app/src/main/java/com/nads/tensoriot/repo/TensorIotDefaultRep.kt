package com.nads.tensoriot.repo

import com.nads.tensoriot.di.ApplicationScope
import com.nads.tensoriot.model.Weathers
import com.nads.tensoriot.model.sevenday.SevenDayForeCast
import com.nads.tensoriot.remote.DataSource
import kotlinx.coroutines.CoroutineScope
import retrofit2.HttpException
import javax.inject.Singleton

@Singleton
class TensorIotDefaultRep(  private val apiSource:DataSource,
                            @ApplicationScope private val externalScope: CoroutineScope
):TensorIotRep {
    override suspend fun getweathers(city:String ,appId:String): Result<Weathers> {
        return try {
            Result.success(apiSource.getWeathers(city , appId))
        }catch (e: HttpException){
            Result.failure(e)
        }
    }
    override suspend fun getweathersSeven(city:String ,count:String,appId:String): Result<SevenDayForeCast> {
        return try {
            Result.success(apiSource.getWeathersSeven(city ,count, appId))
        }catch (e: HttpException){
            Result.failure(e)
        }
    }

}