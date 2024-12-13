package com.example.traincheckinapp.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("mobile")
    var mobile: Long? = null,
    @SerializedName("aadhaar")
    var aadhaar: Long? = null,
    @SerializedName("password")
    var password: String? = null
)

