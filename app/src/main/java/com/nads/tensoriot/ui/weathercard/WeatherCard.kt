package com.nads.tensoriot.ui.weathercard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.nads.tensoriot.R
import com.nads.tensoriot.model.sevenday.Seven

@Composable
fun WeatherCard(index: Int, WeatherItem: Seven,onNavigateToWeather:(Int)->Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp, start = 10.dp)
        .clip(
            shape = RoundedCornerShape(
                10.dp
            )
        )
        .clickable {
        }
        .background(Color(0xfffdedec))
        .padding(10.dp)
        .semantics {
        }) {
            Column(modifier = Modifier.padding(start = 10.dp).clickable{

                onNavigateToWeather(index)
            }) {
                Text(
                    text = "Id " + WeatherItem.dt.toString(),
                    Modifier.fillMaxWidth()
                )
                Text(
                    text = "temp "+ WeatherItem.main.temp.toString(),
                    Modifier.fillMaxWidth()
                )
                Text(
                    text = "description " + WeatherItem.weather[0].description,
                    Modifier.fillMaxWidth()
                )



            }
    }
}


