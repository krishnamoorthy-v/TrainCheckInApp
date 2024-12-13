package com.example.traincheckinapp.models

data class APIResponse(
    val error: Boolean,
    val message: String
)

data class SignUpResponse (
    val error: Boolean,
    val message: String
)

data class LoginResponse (
    val error: Boolean,
    val message: String
)