package com.example.learn_spotify_mvvm_clone.exoplayer.callbacks

import android.app.Service.STOP_FOREGROUND_DETACH
import android.widget.Toast
import com.example.learn_spotify_mvvm_clone.exoplayer.MusicService
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

/**
 * Created by AidenChang 2023/05/24
 */
class MusicPlayerEventListener(
    private val musicService: MusicService
) : Player.Listener {

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (reason == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(STOP_FOREGROUND_DETACH)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "An unknown error occurred", Toast.LENGTH_LONG).show()
    }
}