package com.nads.tensoriot.model.sevenday


import com.google.gson.annotations.SerializedName

data class SevenDayForeCast(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val list: List<Seven>,
    @SerializedName("message")
    val message: Int
)