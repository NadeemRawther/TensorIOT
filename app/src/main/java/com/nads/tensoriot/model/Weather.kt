package com.nads.tensoriot.model

data class Weather(
    val temperature: Double,
    val pressure: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: Double,
    val description: String
)
