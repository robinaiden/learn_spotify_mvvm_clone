package com.example.learn_spotify_mvvm_clone.ui.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learn_spotify_mvvm_clone.data.entities.Song
import com.example.learn_spotify_mvvm_clone.data.other.Constants.Companion.MEDIA_ROOT_ID
import com.example.learn_spotify_mvvm_clone.data.other.Resource
import com.example.learn_spotify_mvvm_clone.exoplayer.MusicServiceConnection
import com.example.learn_spotify_mvvm_clone.exoplayer.isPlayEnabled
import com.example.learn_spotify_mvvm_clone.exoplayer.isPlaying
import com.example.learn_spotify_mvvm_clone.exoplayer.isPrepared
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by AidenChang 2023/05/28
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {
    private val _mediaItem = MutableLiveData<Resource<List<Song>>>()
    val mediaItem: LiveData<Resource<List<Song>>> = _mediaItem

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState

    init {
        _mediaItem.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString(),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()
                    )
                }
                _mediaItem.postValue(Resource.success(items))
            }
        })
    }

    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId ==
            curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}