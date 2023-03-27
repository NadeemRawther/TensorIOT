package com.nads.tensoriot.repo

import com.nads.tensoriot.model.Weathers
import com.nads.tensoriot.model.sevenday.SevenDayForeCast

interface TensorIotRep {
    suspend fun getweathers(city: String, appId: String): Result<Weathers>
    suspend fun getweathersSeven(
        city: String,
        count: String,
        appId: String
    ): Result<SevenDayForeCast>
}