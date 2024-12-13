package com.example.traincheckinapp

import com.example.traincheckinapp.models.Login
import com.example.traincheckinapp.models.APIResponse
import com.example.traincheckinapp.models.LoginResponse
import com.example.traincheckinapp.models.SignUpResponse
import com.example.traincheckinapp.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {

    @Headers("Content-Type: application/json")
//    @POST("/data")

    @POST("account/signup")
    fun signUp(@Body requestBody: User): Call<SignUpResponse>


    @POST("account/login")
    fun login(@Body requestBody: Login): Call<LoginResponse>
}