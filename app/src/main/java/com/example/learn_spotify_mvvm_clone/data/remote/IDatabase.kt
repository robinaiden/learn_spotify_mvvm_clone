package com.example.learn_spotify_mvvm_clone.data.remote

import com.example.learn_spotify_mvvm_clone.data.entities.Song

/**
 * Created by AidenChang 2023/05/06
 */
interface IDatabase {
    suspend fun getAllSongs(): List<Song>
}