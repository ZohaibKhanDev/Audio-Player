package com.example.audioplayer.ui.Screen

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Button
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.audioplayer.realtimedatabase.Message
import org.koin.core.qualifier.named


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileEditScreen(
    navController: NavController,
    userId: String,
    name: String?,
    email: String?,
    password: String?,
    profile: String?
) {
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    var names = name
    var email = email
    var password = password
    val currentUser by remember { mutableStateOf<Message?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = {
                selectedImage = it
            })
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Back")
        }, navigationIcon = {
            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "")
        }, actions = {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "")
        })
    }) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(200.dp)
                    .background(Color.Cyan),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "$profile",
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {},
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 34.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray), contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt,
                            contentDescription = "",
                            modifier = Modifier
                                .size(22.dp)
                                .clickable { launcher.launch("image/*") })
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(value = names.toString(), onValueChange = {
                names = it
            }, label = {
                Text(text = "Name")
            }, colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.30f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.30f),
            )
            )


            Spacer(modifier = Modifier.height(7.dp))
            OutlinedTextField(value = email.toString(), onValueChange = {
                email = it
            }, label = {
                Text(text = "email")
            }, colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.30f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.30f),
            )
            )


            Spacer(modifier = Modifier.height(7.dp))
            OutlinedTextField(value = password.toString(), onValueChange = {
                password = it
            }, label = {
                Text(text = "password")
            }, colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.30f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.30f),
            )
            )


            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(7.dp),
            ) {
                Text(text = "Update")
            }

        }
    }
}

