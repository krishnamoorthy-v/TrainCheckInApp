package com.example.traincheckinapp.models

import com.google.gson.annotations.SerializedName

data class OTP (

    @SerializedName("otp")
    var otp: String? = null
)