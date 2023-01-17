package com.nekkiichi.storyapp.data

sealed class ResponseStatus<out R> private constructor(){
    data class Success<out T>(val data: T): ResponseStatus<T>()
    data class Error(val error: String): ResponseStatus<Nothing>()
    object loading: ResponseStatus<Nothing>()
    object init: ResponseStatus<Nothing>()
    object TokenInvalid: ResponseStatus<Nothing>()
}