package com.leandrolcd.dogedexmvvm.api

import com.leandrolcd.dogedexmvvm.data.model.NetworkCallAnswer
import com.leandrolcd.dogedexmvvm.ui.model.UiStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException


suspend fun <T> makeNetworkCall(dispatcher: CoroutineDispatcher = Dispatchers.IO,call: suspend () -> T): UiStatus<T> {
    return withContext(dispatcher) {
        var msjError: String
        try {
            UiStatus.Success(call())
        } catch (e: HttpException) {
            msjError = e.message()
            UiStatus.Error(message = msjError)
        } catch (e: UnknownHostException) {
            msjError = "El dispositivo no puede conectar con el server, revise la conexión a internet."
            UiStatus.Error(message = msjError)
        }

    }
}

suspend fun <T> makeNetworkCallAnswer(call: () -> Unit): NetworkCallAnswer<T> {
    return withContext(Dispatchers.IO) {
        var msjError = ""
        try {
            NetworkCallAnswer.Success(call())
        } catch (e: HttpException) {
            msjError = e.message()

        } catch (e: UnknownHostException) {
            msjError = "El dispositivo no puede conectar con el server, revise la conexión a internet."

        } catch (e: Exception) {
            msjError =  "Error al intentar conectarse con el server.\n Detalles: ${e.message.toString()}"
        }
        NetworkCallAnswer.Error(message = msjError)
    }
}
