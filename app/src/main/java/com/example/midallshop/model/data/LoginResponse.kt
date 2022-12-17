package com.example.midallshop.model.data

data class LoginResponse(
    var success: Boolean,
    var message: String,
    var token: String,
    var expiresAt: String,

    )
