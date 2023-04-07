package com.leandrolcd.dogedexmvvm.data.model

sealed class NetworkCallAnswer<T>(){

    class Success<T>(val data: T) : NetworkCallAnswer<T>()

    class Error<T>(val message: String) : NetworkCallAnswer<T>()

}
