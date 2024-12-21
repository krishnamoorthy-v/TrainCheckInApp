package com.example.traincheckinapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Base64

object RetrofitClient {




//    var retrofit: Retrofit? = null
//
//    fun getInstance(): Retrofit {
//        val builder = Retrofit.Builder()
////            .baseUrl("https://65ec5f900ddee626c9b019ae.mockapi.io/api/")
//            .baseUrl("http://192.168.235.1:9000/")
//            .addConverterFactory(GsonConverterFactory.create())
//
//
//        retrofit = builder.build()
//        return retrofit as Retrofit

//    private val AUTH = "Basic " + Base64.encodeToString("".toByteArray(), Base64.NO_WRAP)
    private const val BASE_URL = "http://192.168.63.1:9000/"
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            val original = chain.request()
//
//            val requestBuilder = original.newBuilder()
//                .addHeader("Authorization", AUTH)
//                .method(original.method(), original.body())
//
//            val request = requestBuilder.build()
//            chain.proceed(request)
//        }.build()

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }

}