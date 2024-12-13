//package com.example.traincheckinapp.service
//
//import android.util.Log
//import com.example.traincheckinapp.RetrofitClient
//import com.example.traincheckinapp.Api
//import com.example.traincheckinapp.models.Login
//import com.example.traincheckinapp.models.APIResponse
//import okhttp3.ResponseBody
//import org.json.JSONException
//import org.json.JSONObject
//import java.nio.charset.StandardCharsets
//import kotlin.io.encoding.Base64
//
//
//
//fun onLoginButtonClicked(email: String, password: String, callback: LoginCallback) {
//    val loginRequest = Login(email = email.trim(), password = password)
//    val loginCall = RetrofitClient.login(loginRequest)
//
//    loginCall.enqueue(object : retrofit2.Callback<APIResponse> {
//        override fun onResponse(
//            call: retrofit2.Call<APIResponse>,
//            response: retrofit2.Response<APIResponse>
//        ) {
//            when (response.code()) {
//                200 -> {
//                    val successMessage = parseLoginResponse(response.body())
//                    Log.d("Login", "Login successful: $successMessage")
//
//
////                    Log.d("Login", "Login successful: ${response.body()}")
//                    callback.onSuccess(response.body()?.message)
//                }
//
//                400 -> {
//                    val errorMessage = parseErrorResponse(response.errorBody())
//                    Log.e("Login", "Login failed: $errorMessage")
//                    callback.onFailure(errorMessage)
//                }
//
//                else -> {
//                    Log.e("Login", "Unexpected response code: ${response.code()}")
//                    callback.onFailure("Unexpected error")
//                }
//            }
//        }
//
//        override fun onFailure(call: retrofit2.Call<APIResponse>, t: Throwable) {
//            Log.e("Login", "Network error: ${t.message}")
//            callback.onFailure("Network error. Please check your connection.")
//        }
//    })
//}
//
//private fun parseErrorResponse(errorBody: ResponseBody?): String {
//    errorBody?.let {
//        try {
//            val jsonObject = JSONObject(it.string())
//            return jsonObject.optString("Failure", "Unknown error")
//        } catch (e: JSONException) {
//            Log.e("Login", "Error parsing JSON: ${e.message}")
//        }
//    }
//    return "Unknown error"
//}
//
//private fun parseLoginResponse(successBody: APIResponse?): String {
//    successBody?.let {
//        try {
//            val jsonObject = convertJwtToJsonObject(it.message)
//            if (jsonObject != null) {
//                return jsonObject.optString("Success", it.message)
//            } else {
//
//            }
//        } catch (e: JSONException) {
//            Log.e("Login", "Error parsing success JSON: ${e.message}")
//        }
//    }
//    return "unknown success"
//}
//
//interface LoginCallback {
//    fun onSuccess(login: String?)
//    fun onFailure(errorMessage: String)
//}
//
//
//
//fun convertJwtToJsonObject(jwtToken: String): JSONObject? {
//    try {
//        val jwtParts = jwtToken.split(".")
//        val payload = jwtParts[1]
//        val decodedPayload = Base64.decode(payload, Base64.UrlSafe)
//        val payloadString = String(decodedPayload, StandardCharsets.UTF_8)
//        return JSONObject(payloadString)
//    } catch (e: Exception) {
//        Log.e("JWT", "Error converting JWT to JsonObject: ${e.message}")
//        return null // Handle errors gracefully
//    }
//}