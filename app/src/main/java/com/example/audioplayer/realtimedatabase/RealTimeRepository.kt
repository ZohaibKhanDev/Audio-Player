package com.example.audioplayer.realtimedatabase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

class RealTimeRepository(private val db: DatabaseReference) : RealTimeService {
    override suspend fun storeData(message: Message, userId: String): String {
        return try {
            val myRef = db.child("User").child(userId)
            myRef.setValue(message).await()
            "Successful"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    override suspend fun getData(userId: String): Message? {
        return try {
            val myRef = db.child("User").child(userId)
            val dataSnapshot = myRef.get().await()
            dataSnapshot.getValue<Message>()
        } catch (e: Exception) {
            null
        }
    }
}

