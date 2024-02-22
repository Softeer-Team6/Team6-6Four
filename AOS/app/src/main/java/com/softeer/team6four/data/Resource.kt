package com.softeer.team6four.data

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Loading<out T>(val data: T? = null) : Resource<T>()
    data class Error<out T>(val code : Int? = null, val message: String, val data: T? = null) : Resource<T>()
}