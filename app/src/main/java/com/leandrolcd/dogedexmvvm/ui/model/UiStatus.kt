package com.leandrolcd.dogedexmvvm.ui.model

sealed class UiStatus<T>() {

    class Loading<T>() : UiStatus<T>()
    class Loaded<T>() : UiStatus<T>()

    class Error<T>(val message: String) : UiStatus<T>()
    class Success<T>(val data: T) : UiStatus<T>()
}