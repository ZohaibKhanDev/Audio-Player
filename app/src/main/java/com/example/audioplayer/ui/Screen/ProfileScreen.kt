package com.example.audioplayer.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.audioplayer.R
import com.example.audioplayer.realtimedatabase.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val PREFS_NAME = "prefs"
const val PREF_UPLOADED_IMAGE_URL = "uploaded_image_url"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
) {
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var currentUser by remember { mutableStateOf<Message?>(null) }
    var loading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var dialog by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var eye by remember { mutableStateOf(false) }

    if (dialog) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }


    LaunchedEffect(key1 = Unit) {
        uploadedImageUrl = getImageUrlFromPrefs(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri = uri
        })

    LaunchedEffect(Unit) {
        val dbRef = Firebase.database.getReference("User")
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<Message>()
                val user = Firebase.auth.currentUser
                var currentUserEmail: String? = null

                if (user != null) {
                    currentUserEmail = user.email
                }

                for (userSnapshot in snapshot.children) {
                    val message = userSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        userList.add(message)
                        if (message.email == currentUserEmail) {
                            currentUser = message
                        }
                        Log.d("SignUp", "Fetched message: $message")
                    } else {
                        Log.d("SignUp", "Fetched null message")
                    }
                }
                messages = userList
                loading = false
                Log.d("SignUp", "Data fetched: $userList")
            }

            override fun onCancelled(error: DatabaseError) {
                loading = false
                Log.e("SignUp", "Data fetch cancelled or failed", error.toException())
            }
        }
        dbRef.addValueEventListener(userListener)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Profile") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = it.calculateTopPadding(), bottom = 80.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            currentUser?.let {


                LaunchedEffect(key1 = Unit) {
                    it.profileUrl = getImageUrlFromPrefs(context)
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
                            val uploadTask = imageRef.putFile(uri)
                            uploadTask.continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    task.exception?.let { throw it }
                                }
                                imageRef.downloadUrl
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val downloadUrl = task.result
                                    currentUser?.let { user ->
                                        user.profileUrl = downloadUrl.toString()
                                        val userId = Firebase.auth.currentUser?.uid
                                        userId?.let { uid ->
                                            Firebase.database.getReference("User").child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener {
                                                    saveImageUrlToPrefs(
                                                        context,
                                                        user.profileUrl!!
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "Profile com.example.signup.Image Uploaded Successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }.addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to update profile image URL: ${e.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.e(
                                                        "ProfileScreen",
                                                        "Failed to update profile image URL",
                                                        e
                                                    )
                                                }
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to upload image: ${task.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e(
                                        "ProfileScreen",
                                        "Failed to upload image",
                                        task.exception
                                    )
                                }
                                isUploading = false
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                            }
                            isUploading = false
                        }
                    }

                }

                if (it.profileUrl != null) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(200.dp)
                            .background(Color.Cyan),
                        contentAlignment = Alignment.Center
                    ) {

                        it.profileUrl?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                    },
                                contentScale = ContentScale.Crop
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
                                .padding(end = 34.dp, bottom = 16.dp),
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "${currentUser?.name}", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "@_all${currentUser?.password}", fontSize = 15.sp)


                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, start = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .height(53.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF263775)),
                    shape = RoundedCornerShape(11.dp)
                ) {
                    Text(text = "Edit")
                }

                Spacer(modifier = Modifier.height(50.dp))

                Divider()



                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(33.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "")

                        Text(text = "Setting")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = "")

                        Text(text = "Friend")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Group, contentDescription = "")

                        Text(text = "New Group")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SupervisedUserCircle,
                            contentDescription = ""
                        )

                        Text(text = "Support")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Share, contentDescription = "")

                        Text(text = "Share")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.QuestionMark, contentDescription = "")

                        Text(text = "About Us")
                    }
                }
            }
        }
    }
}


fun saveImageUrlToPrefs(context: Context, url: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString(PREF_UPLOADED_IMAGE_URL, url).apply()
}

fun getImageUrlFromPrefs(context: Context): String? {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(PREF_UPLOADED_IMAGE_URL, null)
}

