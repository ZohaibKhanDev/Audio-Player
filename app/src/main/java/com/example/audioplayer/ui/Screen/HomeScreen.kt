package com.example.audioplayer.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.audioplayer.R
import com.example.audioplayer.navigation.Screens
import com.example.audioplayer.realtimedatabase.Message
import com.example.audioplayer.restapi.Audio
import com.example.audioplayer.restapi.Data
import com.example.audioplayer.restapi.MainViewModel
import com.example.audioplayer.restapi.ResultState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    var currentUser by remember { mutableStateOf<Message?>(null) }
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var filteredMessages by remember { mutableStateOf(listOf<Message>()) }
    var searchQuery by remember { mutableStateOf("") }
    val viewModel: MainViewModel = koinInject()
    var isLoading by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var audioData by remember { mutableStateOf<Audio?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAllAudio()
    }

    val state by viewModel.allAudio.collectAsState()

    when (state) {
        is ResultState.Error -> {
            isLoading = false
            val error = (state as ResultState.Error).error
            Text(text = error.toString())
        }

        ResultState.Loading -> {
            isLoading = true
        }

        is ResultState.Success -> {
            isLoading = false
            val success = (state as ResultState.Success).response
            audioData = success
        }
    }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("SignUp", Context.MODE_PRIVATE)
    val sharedPreferencesId = sharedPreferences.getString("userId", null)
    val filteredAudioData = filterAudioData(audioData, searchQuery)

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
                filteredMessages = userList
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
        TopAppBar(title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hello",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                )
                Text(
                    text = "Jhon Johnson",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF171717)),
            actions = {
                if (currentUser?.profileUrl.isNullOrEmpty()) {
                    Image(painter = painterResource(id = R.drawable.person),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate(Screens.Profile.route)
                            }
                    )
                }else{
                    AsyncImage(model = currentUser?.profileUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate(Screens.Profile.route)
                            }
                    )
                }

            })
    }) {


        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0XFF171717)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0XFF171717))
                    .padding(
                        start = 10.dp, end = 10.dp, top = it.calculateTopPadding(), bottom = 100.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 22.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        TextField(
                            value = search,
                            onValueChange = { search = it },
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .weight(1f)
                                .height(53.dp),
                            placeholder = {
                                Text(
                                    text = "Search", color = Color.Gray.copy(alpha = 0.80f)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "",
                                    tint = Color.Gray.copy(alpha = 0.80f)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0XFF333333),
                                unfocusedContainerColor = Color(0XFF333333),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.Gray.copy(alpha = 0.80f),
                                unfocusedTextColor = Color.Gray.copy(alpha = 0.80f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            textStyle = TextStyle(
                                fontSize = 16.sp, fontWeight = FontWeight.W400
                            )
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(9.dp))
                                .width(50.dp)
                                .height(53.dp)
                                .background(Color(0XFF333333)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = "",
                                tint = Color.Gray.copy(alpha = 0.80f)
                            )
                        }
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.poster),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                                .height(230.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Most Popular",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .height(35.dp)
                                .align(Alignment.CenterVertically),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red, contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(text = "view All", color = Color.White)
                        }
                    }
                }

                filteredAudioData?.data?.let { dataList ->
                    items(dataList) { data ->
                        AudioItem(data = data, navController)
                    }
                }
            }
        }

    }


}


fun filterAudioData(audioData: Audio?, query: String): Audio? {
    if (audioData == null || query.isEmpty()) return audioData
    val filteredData = audioData.data?.filter {
        it.title.contains(query, ignoreCase = true)
    }
    return audioData.copy(data = filteredData)
}


@Composable
fun AudioItem(data: Data, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .background(Color(0XFF171717))
            .width(120.dp)
            .height(270.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AsyncImage(model = data.album?.coverXl,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val id = Uri.encode(data.id.toString())
                    val tittle = Uri.encode(data.title)
                    val pic = Uri.encode(data.album?.coverXl)
                    val audio = Uri.encode(data.preview)
                    val name = Uri.encode(data.artist.name)
                    navController.navigate(Screens.Detail.route + "/$id/$tittle/$pic/$audio/$name")
                }
                .height(220.dp))

        Text(text = data.title, color = Color.White, maxLines = 1)

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "",
                modifier = Modifier.size(11.dp),
                tint = Color.Red,
            )
            Text(
                text = "${data.duration}M Download",
                color = Color.Gray,
                fontSize = 11.sp,
                maxLines = 1
            )

        }

    }
}