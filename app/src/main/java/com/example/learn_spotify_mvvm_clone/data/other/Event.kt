package com.example.learn_spotify_mvvm_clone.data.other

/**
 * Created by AidenChang 2023/05/27
 */
open class Event<out T>(private val data: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    fun peekContent() = data
}