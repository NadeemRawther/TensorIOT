package com.nads.tensoriot.ui.authui


import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.nads.tensoriot.ui.mainviewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUi(
    snackbarHostState: SnackbarHostState,
    mainViewModel: MainViewModel,
    auth:FirebaseAuth,
    activity: Activity,
    onNavigatToProfile:() -> Unit,
    onNavigatToSignUp:()->Unit
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
                innerpadding->
               LoginPage(innerpadding,auth,mainViewModel,activity,onNavigatToProfile,onNavigatToSignUp)
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
                        "Login to your App",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )

        },
    )



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    innerPadding: PaddingValues,
    auth: FirebaseAuth,
    mainViewModel: MainViewModel,
    activity: Activity,
    onNavigatToProfile:()-> Unit,
    onNavigatToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val mMaxLength = 30
    var mContext = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height((LocalContext.current.resources
            .displayMetrics.heightPixels / 10).dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                if (it.length <= mMaxLength) email = it
                else Toast.makeText(
                    mContext,
                    "Cannot be more than 30 Characters",
                    Toast.LENGTH_SHORT
                ).show()
            },label = { Text("Email") }, maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {   if (it.length <= mMaxLength) password = it
            else Toast.makeText(
                mContext,
                "Cannot be more than 30 Characters",
                Toast.LENGTH_SHORT)
            },
            label = { Text("Password") }, maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity){
                task->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    onNavigatToProfile()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
        }
        
        ) {
        Text(text = "LOGIN")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "OR")
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
           onNavigatToSignUp()
        }

        ) {
            Text(text = "SIGN UP")
        }
    }
}