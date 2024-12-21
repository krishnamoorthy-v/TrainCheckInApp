package com.example.traincheckinapp

import androidx.compose.ui.graphics.Path
import com.example.traincheckinapp.models.Login
import com.example.traincheckinapp.models.APIResponse
import com.example.traincheckinapp.models.LoginResponse
import com.example.traincheckinapp.models.OTP
import com.example.traincheckinapp.models.OTPResponse
import com.example.traincheckinapp.models.PsgTicketList
import com.example.traincheckinapp.models.SignUpResponse
import com.example.traincheckinapp.models.TicketInfo
import com.example.traincheckinapp.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {

    @Headers("Content-Type: application/json")
//    @POST("/data")

    @POST("account/signup")
    fun signUp(@Body requestBody: User): Call<SignUpResponse>


    @POST("account/login")
    fun login(@Body requestBody: Login): Call<LoginResponse>


    @GET("checkin/sms/otp/{id}")
    fun generateOTP(@retrofit2.http.Path("id") id: String): Call<OTPResponse>

    @POST("checkin/sms/otp/verify/{id}")
    fun verifyOTP(@retrofit2.http.Path("id") id: String, @Body requestBody: OTP): Call<OTPResponse>

    @GET("ticket/passenger/{aadhaar}/{date}")
    fun getPassengerPNRList(@retrofit2.http.Path("aadhaar") aadhaar: String, @retrofit2.http.Path("date") date: String): Call<PsgTicketList>

    @GET("ticket/pnr/{pnr_num}")
    fun getTicket(@retrofit2.http.Path("pnr_num") pnr_num: String): Call<TicketInfo>
}