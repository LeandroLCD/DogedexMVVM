package com.leandrolcd.dogedexmvvm.api

import com.leandrolcd.dogedexmvvm.BASE_URL
import com.leandrolcd.dogedexmvvm.api.models.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
private val httpClient = OkHttpClient
    .Builder()
    .addInterceptor(ApiServiInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


interface ApiService{
    @GET("dogs")
    suspend fun getAllDogs(): DogResponse

    @POST("sign_up")
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AutyResponse

    @POST("sign_in")
    suspend fun login(@Body signInDTO: SignInDTO): AutyResponse
    @Headers("${ApiServiInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @POST("add_dog_to_user")
    suspend fun addDog(@Body addDogToUserDTO: AddDogToUserDTO ): DefaultResponse

    @Headers("${ApiServiInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET("get_user_dogs")
    suspend fun getUserDogs(): DogResponse

}

object DogsApi{
 val retrofitService: ApiService by lazy {
     retrofit.create(ApiService::class.java)
 }
}