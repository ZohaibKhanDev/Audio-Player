package com.example.audioplayer.realtimedatabase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel2(private val repository: Repository2) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    init {
        viewModelScope.launch {
            repository.getMessages().collect { messages ->
                Log.d("MainViewModel2", "Messages retrieved: $messages")
                _messages.value = messages
            }
        }
    }

    fun addMessage(message: Message) {
        viewModelScope.launch {
            try {
                repository.addMessage(message)
                Log.d("MainViewModel2", "Message added: $message")
            } catch (e: Exception) {
                Log.e("MainViewModel2", "Error adding message: ${e.message}", e)
            }
        }
    }

    fun editMessage(userId: String, updatedMessage: Message) {
        viewModelScope.launch {
            try {
                repository.editMessage(userId, updatedMessage)
                Log.d("MainViewModel2", "Message edited: $updatedMessage")
            } catch (e: Exception) {
                Log.e("MainViewModel2", "Error editing message: ${e.message}", e)
            }
        }
    }

    fun getMessageById(userId: String): LiveData<Message?> = liveData {
        emit(repository.getMessageById(userId))
    }
}








class Repository2(private val databaseReference: DatabaseReference) {

    fun getMessages(): Flow<List<Message>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { it.getValue<Message>() }
                trySend(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        databaseReference.addValueEventListener(listener)
        awaitClose { databaseReference.removeEventListener(listener) }
    }

    suspend fun addMessage(message: Message) {
        try {
            databaseReference.push().setValue(message).addOnFailureListener {
                throw it
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun editMessage(userId: String, updatedMessage: Message) {
        try {
            databaseReference.child(userId).setValue(updatedMessage).addOnFailureListener {
                throw it
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getMessageById(userId: String): Message? {
        return try {
            val snapshot = databaseReference.child(userId).get().await()
            snapshot.getValue(Message::class.java)
        } catch (e: Exception) {
            null
        }
    }
}






