package com.example.audioplayer.ui.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AboutUs(navController: NavController) {
    val verticalScroll= rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(verticalScroll)
            .padding(20.dp)
    ) {
        Text(text = "About Us", fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Welcome to [Audio Player], your ultimate destination for a seamless music experience. Our mission is to provide a platform where music lovers can enjoy their favorite tunes, whether stored locally on their devices or streamed online from the cloud.", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Our Story", fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(30.dp))
        
        Text(text = "Local Storage Playback: Enjoy your personal music collection with high-quality playback. Our app supports various audio formats, ensuring you can listen to all your favorite tracks without any hassle.\n" +
                "Online Streaming: Access a vast library of songs from our online database. Whether you're in the mood for the latest hits or classic tunes, we have something for everyone.\n" +
                "User-Friendly Interface: Our intuitive design makes it easy to navigate through your music collection. With just a few taps, you can find, play, and organize your favorite tracks.\n" +
                "Seamless Integration: Switch between local and online music effortlessly. Our app ensures a smooth transition, so you never miss a beat.\n" +
                "Customizable Playlists: Create and manage playlists to suit your mood and preferences. Whether it's a workout mix or a chill playlist, we've got you covered.\n" +
                "Advanced Features: Take advantage of features like equalizer settings, playback speed control, and more to enhance your listening experience.")

        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Our Vision", fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "At [Audio Player], we believe that music should be accessible, enjoyable, and personal. We are constantly innovating to bring you new features and improvements, ensuring that your music experience is always top-notch.")


        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Join Our Community", fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(30.dp))
    
        Text(text = "Become a part of our growing community of music lovers. Stay updated with the latest app features, share your feedback, and connect with fellow users. Follow us on social media and be the first to know about our latest updates and exclusive content.")
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Contact Us", fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(30.dp))
        
        Text(text = "We'd love to hear from you! Whether you have questions, feedback, or just want to say hello, feel free to reach out to us at [Your Contact Email]. Your input helps us make [Your App Name] better with each update.")
        Spacer(modifier = Modifier.height(50.dp))
    
        Text(text = "Thank you for choosing [Audio Player]. Let's create beautiful music memories together!")
    }
}