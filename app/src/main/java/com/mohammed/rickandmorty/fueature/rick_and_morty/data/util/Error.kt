package com.mohammed.rickandmorty.fueature.rick_and_morty.data.util

sealed interface Error {
    sealed interface Local : Error {
        data object DiskFull : Local
        data class Unknown(val message: String) : Local
    }
    sealed interface Remote : Error {
        data object NoInternet : Remote
        data object NotFound : Remote
        data object JsonParseError : Remote
        data object BadRequest : Remote
        data object Unauthorized : Remote
        data object Timeout : Remote
        data object TooManyRequests : Remote
        data object ServerError : Remote
        data class Unknown(val message: String) : Remote
    }
}