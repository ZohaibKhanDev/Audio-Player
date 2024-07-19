package acom.example.audioplayer.ui.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.audioplayer.realtimedatabase.MainViewModel2
import com.example.audioplayer.realtimedatabase.Message
import com.example.audioplayer.realtimedatabase.Repository2
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEdit(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var currentUser by remember { mutableStateOf<Message?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            val dbRef = Firebase.database.getReference("User").child(userId)
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUser = snapshot.getValue(Message::class.java)
                    currentUser?.let {
                        name = it.name
                        email = it.email
                        password = it.password
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProfileEdit", "Failed to load user data", error.toException())
                }
            })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val user = Firebase.auth.currentUser
                    val userId = user?.uid
                    if (userId != null) {
                        val updatedUser = currentUser?.copy(name = name, email = email, password = password)
                        if (updatedUser != null) {
                            Firebase.database.getReference("User").child(userId)
                                .setValue(updatedUser)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                    Log.e("ProfileEdit", "Failed to update profile", e)
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF263775)),
                shape = RoundedCornerShape(11.dp)
            ) {
                Text(text = "Update")
            }
        }
    }
}






