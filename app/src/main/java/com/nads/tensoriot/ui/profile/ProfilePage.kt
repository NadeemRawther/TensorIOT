package com.nads.tensoriot.ui.profile

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.nads.tensoriot.BuildConfig
import com.nads.tensoriot.R
import com.nads.tensoriot.ui.mainviewmodel.MainViewModel
import com.nads.tensoriot.ui.weathercard.WeatherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
  snackbarHostState: SnackbarHostState,
  viewModel: MainViewModel,
  auth: FirebaseAuth,
  activity: Activity,
  firestore: FirebaseFirestore,
  storageRef: StorageReference,
  onNavigateToLogin:()->Unit,
  onNavigateToWeatherDetails:()->Unit
) {

  var picture by remember { mutableStateOf("https://images.unsplash.com/photo-1668174206552-cc53001e480b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=386&q=80") }
  var username by remember { mutableStateOf("UserName")}
  var shortbio by remember { mutableStateOf("Bio")}
  val mMaxLength = 20
  val appId = BuildConfig.MY_API_KEY
  val mMaxLengthEmail = 35
  val mContext = LocalContext.current
  val uid = auth.currentUser?.uid
  var city by remember { mutableStateOf("") }
   uid?.let { firestore.collection("nads").document(it).get()
    .addOnCompleteListener { task ->
    if (task.isSuccessful) {
      val document1 = (task.result.data?.get("imageurl").toString())!!
      username = (task.result.data?.get("username").toString())
      shortbio = (task.result.data?.get("shortbio").toString())
      picture = document1
    } else {
      Log.d(TAG, "Cached get failed: ", task.exception)
    }
  }}

  Column() {
//    viewModel.getSevenDayWeather("Kolkata,in","7",appId)
    var getWeather = viewModel.weatherSeven.collectAsState()

    Box(modifier = Modifier
      .fillMaxWidth()
      ){
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Gray)
        .height((LocalContext.current.resources.displayMetrics.heightPixels / 9).dp))

      Column(modifier= Modifier
        .fillMaxSize()
        .background(Color.Transparent)
        , horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = {
          auth.signOut()
          onNavigateToLogin()
                             },modifier= Modifier
          .padding(start = 265.dp, top = 15.dp)
          .size(50.dp)) {
          Icon(painter = painterResource(id = R.drawable.logouticon)
            , contentDescription = "Logout")
        }
        Spacer(modifier = Modifier
          .fillMaxWidth()
          .height((LocalContext.current.resources.displayMetrics.heightPixels / 18).dp))
        Image(
          painter = rememberImagePainter(data = picture),
          contentDescription = "just a profile avatar image",
          contentScale = ContentScale.Fit,
          modifier = Modifier
            .clip(CircleShape)
            .size(100.dp),
        )
        Text(text = username
          , textAlign = TextAlign.Center
          , fontWeight = FontWeight.Bold, fontSize = 25.sp)
        Spacer(modifier = Modifier
          .fillMaxWidth()
          .height(15.dp))
        Text(text = shortbio
          , textAlign = TextAlign.Center
          , fontWeight = FontWeight.Medium, fontSize = 20.sp)
        val listState = rememberLazyListState()
        Spacer(modifier = Modifier
          .fillMaxWidth()
          .height(15.dp))
        Row() {
          OutlinedTextField(
            modifier= Modifier.padding(start=10.dp,end=10.dp).width(150.dp).height(60.dp),
            value = city,
            onValueChange = {
              if (it.length <= mMaxLengthEmail) city = it
              else Toast.makeText(mContext, "Cannot be more than 35 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("City Name") }
            , maxLines = 1
          )

          Button(modifier= Modifier.padding(start=10.dp,end=10.dp),
            onClick = {
              viewModel.getSevenDayWeather(city,"7"
                , appId =appId )
            }) {
            Text("Search")
          }


        }


        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp), state = listState,
          modifier = Modifier.padding(10.dp)) {
             itemsIndexed(getWeather.value.list) { index,WeatherItem ->
                  WeatherCard(index,WeatherItem, onNavigateToWeather = {
                    viewModel.isIndex.value = it
                    onNavigateToWeatherDetails()
                  })
          }
        }
      }

    }

  }  
}
