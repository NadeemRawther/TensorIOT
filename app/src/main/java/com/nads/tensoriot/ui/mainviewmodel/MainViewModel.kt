package com.nads.tensoriot.ui.mainviewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nads.tensoriot.model.*
import com.nads.tensoriot.model.sevenday.City
import com.nads.tensoriot.model.sevenday.Rain
import com.nads.tensoriot.model.sevenday.Seven
import com.nads.tensoriot.model.sevenday.SevenDayForeCast
import com.nads.tensoriot.repo.TensorIotRep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( private val tensorIotRep: TensorIotRep
                                         ,private val savedStateHandle: SavedStateHandle) :ViewModel()  {
    private val _isLogin = MutableStateFlow(false)
    val isLogin: MutableStateFlow<Boolean> get() = _isLogin
    private val _weather = MutableStateFlow(Weathers(
        "",
        Clouds(0),0, Coord(0.1,0.1),
        0,0, Main(0.1,0,0,0.1,0.1,0.1)
    ,"", Sys("",0,0,0,0)
    , 0,0, listOf(WeatherX("","",0,"")),
        Wind(0,0.1)
    ))
    val weather:MutableStateFlow<Weathers> get() = _weather
    private val _weatherSeven = MutableStateFlow(SevenDayForeCast(City(
        com.nads.tensoriot.model.sevenday.Coord(0.1,0.1),"",0
    ,"",100,1,1,1,
    ),7,"", listOf(Seven(com.nads.tensoriot.model.sevenday.Clouds(0),
    0,"",com.nads.tensoriot.model.sevenday.Main(
            0.1,1,1,1,1,0.1,0.1,0.1,0.1
    ),0.1, Rain(0.1),com.nads.tensoriot.model.sevenday.Sys(""),0, listOf(
            com.nads.tensoriot.model.sevenday.Weather("","",0,"")),com.nads.tensoriot.model.sevenday.Wind(
            0,0.1,0.1
            )
    )),0
    ))
    val weatherSeven:MutableStateFlow<SevenDayForeCast> get() = _weatherSeven
    private val _isIndex = MutableStateFlow(0)
    val isIndex: MutableStateFlow<Int> get() = _isIndex


    fun getWeathers(city:String,appId:String){
        viewModelScope.launch {
            val data =tensorIotRep.getweathers(city,appId)
            if (data.isSuccess){
                   data.map {
                       weather.value = it
                   }
            }
        }

    }
    fun getSevenDayWeather(city: String,count:String,appId: String){
        viewModelScope.launch {
            val data = tensorIotRep.getweathersSeven(city,count,appId)
            if (data.isSuccess){
                data.map {
                    weatherSeven.value = it
                }
            }
        }
    }


}