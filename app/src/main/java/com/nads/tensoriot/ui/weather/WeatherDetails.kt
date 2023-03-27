package com.nads.tensoriot.ui.weather

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nads.tensoriot.ui.mainviewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetails(
    snackbarHostState: SnackbarHostState,
    viewModel: MainViewModel,
    activity: Activity
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
                innerpadding->
            WeatherDt(viewModel = viewModel,snackbarHostState,activity,innerpadding)
//            if (error.value){
//                LaunchedEffect(key1 = snackbarHostState ){
//                    snackbarHostState.showSnackbar("You Can't add more than 4 Items Kindly \n " +
//                            "Change old values to Select to add new values")
//                }
//            }
//            if (less.value){
//                LaunchedEffect(key1 = snackbarHostState ){
//                    snackbarHostState.showSnackbar("You Should add 4 Items")
//                }
//
//            }

        },
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar(

                title = {
                    Text(
                        "Weather Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.background(Color.Gray)
            )

        }
    )







}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WeatherDt(
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState,
    activity: Activity,
    innerpadding: PaddingValues
) {
    Column(modifier = Modifier.fillMaxWidth().padding(innerpadding)
        , horizontalAlignment = Alignment.CenterHorizontally
    , verticalArrangement = Arrangement.Center) {
        val data  = viewModel.weatherSeven.collectAsState()
        val data2 = data.value.list[viewModel.isIndex.value]
        Text(
            text = "Id: " + data2.dt.toString(),
            Modifier.fillMaxWidth().padding(start=15.dp)
        )
        Text(
            text = "temp: "+ data2.main.temp.toString(),
            Modifier.fillMaxWidth().padding(start = 15.dp)
        )

        Text(
            text = "feels like: " + data2.main.feelsLike,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "temp min: " + data2.main.tempMin,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "temp Max: " + data2.main.tempMax,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "pressure: " + data2.main.pressure,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "sea level: " + data2.main.seaLevel,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "ground level: " + data2.main.grndLevel,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "humidity: " + data2.main.humidity,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "temp kf: " + data2.main.tempKf,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "main: " + data2.weather[0].main,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "Description : " + data2.weather[0].description,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "clouds: " + data2.clouds.all,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "wind speed: " + data2.wind.speed,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "deg: " + data2.wind.deg,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
        Text(
            text = "gust: " + data2.wind.gust,
            Modifier.fillMaxWidth().padding(start = 15.dp),

            )
       if (data2.rain != null ){
           Text(
               text = "rain: ${data2.rain.h}",
               Modifier.fillMaxWidth().padding(start = 15.dp),

               )
       }



    }


}