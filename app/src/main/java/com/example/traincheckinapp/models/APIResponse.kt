package com.example.traincheckinapp.models

import java.time.LocalDateTime

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
    val message: UserInfo
)

data class OTPResponse (
    val error: Boolean,
    val message: String
)



data class UserInfo (
    val id: String,
    val name: String,
    val email: String,
    val mobile: Number,
    val aadhaar: Number,
    val password: String
)


data class PsgTicketList (
    val aadhaar: Number,
    val pnr_number: List<Long>
)

data class TicketInfo (
    val pnr_number: String,
    val boarding_point: String,
    val droping_point: String,
    val train_time: String
)