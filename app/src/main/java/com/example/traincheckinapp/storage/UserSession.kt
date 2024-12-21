package com.example.traincheckinapp.storage

object UserSession {
    var userId: String? = null
    var email: String? = null
    var aadhaar: Number? = null
    var mobile: Number? = null
    var pnr_list: List<Long>? = null
    var pnr_clicked: String? = null
}