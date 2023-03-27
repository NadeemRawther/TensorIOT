package com.nads.tensoriot.ui.authui

import android.app.Activity
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.nads.tensoriot.R
import com.nads.tensoriot.ui.mainviewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpUi(
    snackbarHostState: SnackbarHostState
    , mainViewModel: MainViewModel, auth: FirebaseAuth
    , activity: Activity,
    firestore: FirebaseFirestore,
    storageRef: StorageReference,
    onNavigateToProfile: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
                innerpadding->
           SignUpPage(innerpadding,auth, activity,firestore,storageRef,onNavigateToProfile)
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
                        "Register",
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
fun SignUpPage(
    innerpadding: PaddingValues,
    auth: FirebaseAuth,
    activity: Activity,
    firestore: FirebaseFirestore,
    storageRef: StorageReference,
    onNavigateToProfile:()->Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var shortbio by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
              imageUri = uri

        }
    )
    var painter: Painter = rememberImagePainter(data = R.drawable.img)
    val mMaxLength = 20
    val mMaxLengthEmail = 35
    if (imageUri != null) painter = rememberImagePainter(data = imageUri) else painter = painter
    // fetching local context
    val mContext = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(innerpadding)
        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
        .verticalScroll(rememberScrollState())
    , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Row(modifier = Modifier.fillMaxWidth()
            ,horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painter,
                contentDescription = "just a profile avatar image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Set Display Picture")
        }
     
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                if (it.length <= mMaxLengthEmail) email = it
                else Toast.makeText(mContext, "Cannot be more than 20 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("Email") }
        , maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                if (it.length <= mMaxLength) password = it
                else Toast.makeText(mContext, "Cannot be more than 10 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("Password") },
             maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = confirmpassword,
            onValueChange = {
                if (it.length <= mMaxLength) confirmpassword = it
                else Toast.makeText(mContext, "Cannot be more than 10 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("confirmPassword") }
            , maxLines = 1, singleLine = true
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {
            if (it.length <= mMaxLength) username = it
            else Toast.makeText(mContext, "Cannot be more than 10 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("UserName") }
            , maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = shortbio,
            onValueChange = {
            if (it.length <= mMaxLength) shortbio = it
            else Toast.makeText(mContext, "Cannot be more than 10 Characters", Toast.LENGTH_SHORT).show()
            },
            label = { Text("ShortBio") }
            , maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val tensorRef = storageRef.child("images/$username.jpg")
                        val data = imageUri?.let { it1 -> tensorRef.putFile(it1) }
                        data?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "Image Successfull.",
                                    Toast.LENGTH_SHORT).show()
                                tensorRef.downloadUrl.addOnSuccessListener {
                                    val downloaduri = it.toString()
                                    val userdata = hashMapOf(
                                        "username" to username,
                                        "shortbio" to shortbio,
                                        "imageurl" to downloaduri,
                                    )
                                    firestore.collection("nads").document(uid.toString())
                                        .set(userdata)
                                        .addOnSuccessListener {
                                            onNavigateToProfile()
                                            Log.d(TAG, "DocumentSnapshot successfully written!")
                                        }
                                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                                }
                            } else {
                                // Handle the upload failure
                            }
                        }
                        data?.addOnProgressListener { taskSnapshot ->
                            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                            // Handle the upload progress
                        }
                        data?.addOnFailureListener(OnFailureListener {
                            Log.e(TAG,it.toString())
                        })

                    } else {
                        Toast.makeText(activity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }

        ) {
            Text(text = "SIGN UP")
        }
    }
}
