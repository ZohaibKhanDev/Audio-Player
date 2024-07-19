package acom.example.audioplayer.ui.Screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.audioplayer.realtimedatabase.MainViewModel2
import com.example.audioplayer.realtimedatabase.Message
import com.example.audioplayer.realtimedatabase.Repository2
import com.google.firebase.Firebase
import com.google.firebase.database.database

@Composable
fun ProfileEdit(navController: NavController) {
    val myRef = Firebase.database.getReference("User")
    val repository = remember {
        Repository2(myRef)
    }
    val realTimeViewModel = remember {
        MainViewModel2(repository)
    }
    val context = LocalContext.current
    val currentUser by remember { mutableStateOf<Message?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("SignUp", Context.MODE_PRIVATE)
    val sharedPreferencesId = sharedPreferences.getString("userId", null)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter your name") }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Enter your email") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                val updateMessage = Message(
                    currentUser?.userId.toString(),
                    currentUser?.profileUrl.toString(), name, email, password
                )
                realTimeViewModel.editMessage(sharedPreferencesId.toString(), updateMessage)
                navController.navigateUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }


    }
}
