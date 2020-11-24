package com.example.practice_demo.network

import com.example.practice_demo.login.data.model.RefreshTokenRequest
import com.example.practice_demo.login.data.model.UserLoginRequest
import com.example.practice_demo.login.data.model.UserLoginResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://api.mcomputing.eu/mobv/service.php/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

enum class ServiceAction(val action: String) {
    REGISTER("register"),
    INFO("userProfile"),
    LOGIN("login"),
    REFRESH_TOKEN("refreshToken"),
    CHANGE_PASSWORD("password"),
    CLEAR_PHOTO("clearPhoto"),
    GET_POSTS("posts"),
    USER_EXISTS("exists"),
    DELETE_POST("deletePost"),
}

interface ApiService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("service.php")
    suspend fun loginService(@Body body: UserLoginRequest): UserLoginResponse

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("service.php")
    suspend fun refreshTokenService(@Body body: RefreshTokenRequest): UserLoginResponse
}

object Api {
    val retrofitService: ApiService by lazy {
       retrofit.create(ApiService::class.java)
    }

    //Todo: uskladnit na safe miesto
    const val API_KEY = "kS3lX8pX2aM0rM4hA7kE1aU9sP5rL3"
}