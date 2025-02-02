package com.example.audioplayer.loginauth.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.audioplayer.R
import com.example.audioplayer.loginauth.AuthViewModel
import com.example.audioplayer.loginauth.User
import com.example.audioplayer.navigation.Screens
import com.example.audioplayer.realtimedatabase.MainViewModel2
import com.example.audioplayer.realtimedatabase.Message
import com.example.audioplayer.realtimedatabase.Repository2
import com.example.audioplayer.restapi.ResultState
import com.example.audioplayer.ui.Screen.getImageUrlFromPrefs
import com.example.audioplayer.ui.Screen.saveImageUrlToPrefs
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var checkBox by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = koinInject()
    val myRef = Firebase.database.getReference("User")
    val repository = remember {
        Repository2(myRef)
    }
    val realTimeViewModel = remember {
        MainViewModel2(repository)
    }

    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var eye by remember { mutableStateOf(false) }
    var eye1 by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 70.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(key1 = Unit) {
            uploadedImageUrl = getImageUrlFromPrefs(context)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                selectedImageUri = uri
            })

        selectedImageUri?.let { uri ->
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("image/${uri.lastPathSegment}")

            LaunchedEffect(key1 = uri) {
                try {
                    isUploading = true
                    imageRef.putFile(uri).await()
                    val downloadUrl = imageRef.downloadUrl.await()
                    uploadedImageUrl = downloadUrl.toString()
                    saveImageUrlToPrefs(context, uploadedImageUrl!!)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Image Uploaded Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    isUploading = false
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Spacer(modifier = Modifier.height(11.dp))

            if (uploadedImageUrl != null) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(200.dp)
                        .background(Color.Cyan),
                    contentAlignment = Alignment.Center
                ) {

                    uploadedImageUrl?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                        )
                    }
                    if (isUploading) {
                        Box(
                            modifier = Modifier.size(201.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize(),
                                color = Color.White,
                                trackColor = Color.Red
                            )
                        }

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 37.dp, bottom = 16.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(22.dp)
                                    .clickable { launcher.launch("image/*") }

                            )
                        }
                    }
                }
            } else {
                Box(

                    modifier = Modifier
                        .clip(CircleShape)
                        .size(200.dp)
                        .background(Color.Cyan),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 37.dp, bottom = 16.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(22.dp)
                                    .clickable { launcher.launch("image/*") }

                            )
                        }
                    }

                }

            }


            Spacer(modifier = Modifier.height(11.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(13.dp))
                Text(text = "Name", modifier = Modifier.align(Alignment.Start))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text(text = "ex: Jon Smith", fontSize = 15.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(9.dp),
                    textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W400),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                    )
                )

                Spacer(modifier = Modifier.height(13.dp))
                Text(text = "Email", modifier = Modifier.align(Alignment.Start))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(text = "ex: jon.smith@email.com", fontSize = 15.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(9.dp),
                    textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W400),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                    )
                )

                Spacer(modifier = Modifier.height(13.dp))

                Text(text = "Password", modifier = Modifier.align(Alignment.Start))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(text = "*********", fontSize = 15.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(9.dp),
                    textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W400),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                    ), trailingIcon = {
                        if (password >= 1.toString()){

                            Icon(
                                imageVector = if (eye) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "",
                                modifier = Modifier.clickable { eye = !eye }
                            )
                        }
                    }, visualTransformation = if (eye) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(13.dp))
                Text(text = "Confirm password", modifier = Modifier.align(Alignment.Start))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = {
                        Text(text = "*********", fontSize = 15.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(9.dp),
                    textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W400),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                    ), trailingIcon = {
                        if (confirmPassword >= 1.toString()){

                            Icon(
                                imageVector = if (eye1) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "",
                                modifier = Modifier.clickable { eye1 = !eye1}
                            )
                        }
                    }, visualTransformation = if (eye1) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(9.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkBox,
                        onCheckedChange = { checkBox = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0XFF00B140), uncheckedColor = Color(0XFF00B140)
                        )
                    )

                    Text(text = "I understood the ")

                    Text(text = "terms & policy.", color = Color(0XFF00B140))
                }
                Spacer(modifier = Modifier.height(10.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            if (password.length < 6 || !email.contains("@") || !checkBox) {
                                Toast.makeText(
                                    context,
                                    "Please Enter Valid Email, Password, and Accept Terms",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (password != confirmPassword) {
                                Toast.makeText(
                                    context, "Passwords do not match", Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                scope.launch {
                                    authViewModel.loginUser(
                                        User(email, password)
                                    ).collect {
                                        when (it) {
                                            is ResultState.Error -> {
                                                isLoading = false
                                                Toast.makeText(
                                                    context, "${it.error}", Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            ResultState.Loading -> {
                                                isLoading = true
                                            }

                                            is ResultState.Success -> {
                                                val userId = Firebase.auth.currentUser?.uid
                                                navController.navigate(Screens.MainScreen.route)
                                                isLoading = false
                                                Toast.makeText(
                                                    context, it.response, Toast.LENGTH_SHORT
                                                ).show()
                                                println("userid$userId")
                                                val data = Message(
                                                    userId.toString(),
                                                    profileUrl = selectedImageUri.toString(),
                                                    name,
                                                    email,
                                                    password
                                                )
                                                realTimeViewModel.addMessage(data)

                                            }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color(0XFF00B140), contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(11.dp)
                    ) {
                        Text(text = "Login")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "or sign up with", color = Color(0XFF888888))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = "Instagram",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.linkedin),
                        contentDescription = "LinkedIn",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.pinterest),
                        contentDescription = "Pinterest",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

        }
    }
}



