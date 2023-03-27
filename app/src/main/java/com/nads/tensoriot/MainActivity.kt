package com.nads.tensoriot

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.nads.tensoriot.ui.authui.LoginUi
import com.nads.tensoriot.ui.authui.SignUpUi
import com.nads.tensoriot.ui.mainviewmodel.MainViewModel
import com.nads.tensoriot.ui.profile.ProfilePage
import com.nads.tensoriot.ui.theme.TensorIOTTheme
import com.nads.tensoriot.ui.weather.WeatherDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        // Create a storage reference from our app
        var storage = Firebase.storage("gs://tensoriot-381510.appspot.com")
        var storageRef = storage.reference
        val firestore = Firebase.firestore

        val currentUser = auth.currentUser
        if(currentUser != null){
            mainViewModel.isLogin.value = true
        }
        setContent {
            TensorIOTTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val navController = rememberNavController()
                val scrollviewBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TensorIOTNavHost(
                        navController = navController,
                        viewModel = mainViewModel,
                        snackbarHostState = snackbarHostState,
                        scope = scope,
                        inner_Padding = PaddingValues(10.dp),
                        activity = LocalContext.current as Activity,
                        auth,
                        firestore,
                        storageRef
                    )
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TensorIOTNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    inner_Padding: PaddingValues,
    activity: Activity,
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    storageRef: StorageReference

){
    var startDestination = "login"
    if (viewModel.isLogin.value){
        startDestination = "profile"
    }
    else
    {
        startDestination="login"
    }
    NavHost(navController = navController, startDestination = startDestination
        , modifier = Modifier.semantics { testTag = "start1" },){

        composable("login"){
          LoginUi(snackbarHostState, viewModel,auth,activity,
              onNavigatToSignUp = {navController.navigate("signup")},
              onNavigatToProfile = {navController.navigate("profile")})


        }
        composable("signup"){
            SignUpUi(snackbarHostState, viewModel,auth,activity, firestore,storageRef) {
                navController.navigate(
                    "profile"
                )
            }
        }
        composable("profile"){
          ProfilePage(snackbarHostState,viewModel,auth,activity,firestore,storageRef,
              onNavigateToWeatherDetails = {navController.navigate("weatherdetails")}
              , onNavigateToLogin = { navController.navigate("login") {
                  popUpTo("login")
              }})


        }
        composable( "weatherdetails"){
           WeatherDetails(snackbarHostState, viewModel,activity)
        }

    }



}