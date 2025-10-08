package com.harry.phrasebook.core.model

sealed interface Result<out T> {
    data class Ok<out T>(val value: T) : Result<T>
    data class Err(val throwable: Throwable) : Result<Nothing>
}