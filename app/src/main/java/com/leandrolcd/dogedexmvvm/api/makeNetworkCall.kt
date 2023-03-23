package com.leandrolcd.dogedexmvvm.api

import android.util.Log
import com.leandrolcd.dogedexmvvm.dogslist.UiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(
    call: suspend () -> T
): UiStatus<T> {
    return withContext(Dispatchers.IO) {
        var msjError:String
        try {
            UiStatus.Success(call())
        } catch (e: HttpException) {
            if (e.code() == 401) {
                msjError = "Usuario o contraseña invalido."
            } else {
                Log.i("http", "${e.code()} ${e.message()}")
                msjError = e.message()
            }
            UiStatus.Error(message = msjError)
        } catch (e: UnknownHostException) {
            msjError =
                "El dispositivo no puede conectar con el server, revise la conexión a internet."
            UiStatus.Error(message = msjError)
        } catch (e: Exception) {
            msjError = when (e.message) {
                "sign_up_error" -> "Credenciales invalidas"
                "sign_in_error" -> "Error en el Login in"
                "user_not_found" -> "Usuario no registrado"
                "user_already_exists" -> "El usuario ya existe"
                "error_adding_dog" -> e.message.toString()
                else -> "Error al intentar conectarse con el server.\n Detalles: ${e.message.toString()}"
            }
            UiStatus.Error(message = msjError)
        }

    }
}