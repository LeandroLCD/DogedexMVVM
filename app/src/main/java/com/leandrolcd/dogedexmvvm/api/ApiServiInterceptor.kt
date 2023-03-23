package com.leandrolcd.dogedexmvvm.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

object ApiServiInterceptor:Interceptor {
    const val NEEDS_AUTH_HEADER_KEY = "needs_authentication"
    private var sessionToken:String? = null
    fun setSessionToken(sessionToken:String){
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val header = request.headers(NEEDS_AUTH_HEADER_KEY)
        if(header != null && header.contains("true")){
            if(sessionToken == null){
                throw RuntimeException("Requiere Token de Autenticación")
            }else{
                requestBuilder.addHeader("AUTH-TOKEN", sessionToken!!)
                Log.i("Headers", sessionToken.toString())
            }
        }
        Log.i("Headers", header.toString())

        return chain.proceed(requestBuilder.build())
    }
}