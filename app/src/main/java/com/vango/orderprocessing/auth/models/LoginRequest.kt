package com.vango.orderprocessing.auth.models

data class LoginRequest(
    val username: String,
    val password: String
)