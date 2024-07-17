package com.example.audioplayer.loginauth.screens

import android.content.Context
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.audioplayer.restapi.ResultState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun MainScreen(navController: NavController) {
    val authviewModel: AuthViewModel = koinInject()
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember {
        mutableStateOf(false)
    }
    var eye by remember {
        mutableStateOf(false)
    }
    val userId=Firebase.auth.currentUser?.uid

    val sharedPreferences = context.getSharedPreferences("SignUp", Context.MODE_PRIVATE)
    val sharedPreferencesId = sharedPreferences.getString("userId", null)

    LaunchedEffect(key1 = Unit) {
        if (sharedPreferencesId != null) {
            navController.navigate(Screens.Home.route) {
                popUpTo(Screens.MainScreen.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.welcomepic),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = "Welcome", color = Color.Black, fontSize = 21.sp, fontWeight = FontWeight.W500)
        Text(text = "By signing in you are agreeing our")
        Text(text = "Term and privacy policy",
            color = Color(0XFF036BB9),
            modifier = Modifier
                .padding(bottom = 33.dp)
                .clickable { })

        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            TextField(value = email,
                onValueChange = {
                    email = it
                },
                placeholder = {
                    Text(
                        text = "Enter Email",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.DarkGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .height(52.dp),
                shape = RoundedCornerShape(9.dp),
                textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W400),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "")
                })
            Spacer(modifier = Modifier.height(30.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text(
                        text = "Password",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.DarkGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .height(52.dp),
                shape = RoundedCornerShape(9.dp),
                textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W400),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.70f),
                    focusedTextColor = Color.DarkGray,
                    unfocusedTextColor = Color.DarkGray,
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "")
                },
                trailingIcon = {
                    if (password >= 1.toString()) {
                        Icon(imageVector = if (eye) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = "",
                            modifier = Modifier.clickable { eye = !eye })
                    } else {

                    }
                },
                visualTransformation = if (eye) VisualTransformation.None else PasswordVisualTransformation()
            )
            Text(
                text = "Forgot Password?",
                color = Color(0XFF6358DC),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 3.dp, end = 12.dp)
                    .clickable {  }
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (password <= 9.toString() && email.contentEquals("@gmail.com")) {
                        Toast.makeText(
                            context, "Please Enter Email And Password", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        scope.launch {
                            authviewModel.signUpUser(
                                User(email, password)
                            ).collect {
                                when (it) {
                                    is ResultState.Error -> {
                                        isLoading = false
                                        Toast.makeText(context, "${it.error}", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                    ResultState.Loading -> {
                                        isLoading = true
                                    }

                                    is ResultState.Success -> {
                                        isLoading = false
                                        Toast.makeText(context, it.response, Toast.LENGTH_SHORT)
                                            .show()
                                        val sharedPreferences = context.getSharedPreferences("SignUp", Context.MODE_PRIVATE)
                                        sharedPreferences.edit().putString("userId", userId).apply()
                                        navController.navigate(Screens.Home.route)
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(320.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF0386D0))
            ) {
                Text(text = "SignUp")
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don’t have an account?", fontSize = 15.sp
                )

                Text(
                    text = "Register",
                    color = Color(0XFF6358DC),
                    modifier = Modifier.clickable {navController.navigate(Screens.LoginScreen.route) }
                )
            }

        }


        Text(text = "Login With", fontWeight = FontWeight.Bold)


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.facebook), contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    ),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.instagram), contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    ),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.linkedin), contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    ),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.pinterest), contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    ),
                contentScale = ContentScale.Crop,
            )
        }


    }
}