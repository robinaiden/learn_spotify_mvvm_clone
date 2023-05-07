package com.example.learn_spotify_mvvm_clone.data.remote

import com.example.learn_spotify_mvvm_clone.data.entities.Song
import com.example.learn_spotify_mvvm_clone.data.other.Constants.Companion.SONG_COLLECTION
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * Created by AidenChang 2023/05/06
 */
class MusicDatabase : IDatabase {
    private val firestore = Firebase.firestore
    private val songCollection = firestore.collection(SONG_COLLECTION)

    override suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}