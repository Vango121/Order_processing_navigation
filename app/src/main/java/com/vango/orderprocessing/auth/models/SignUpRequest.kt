package com.vango.orderprocessing.auth.models

data class SignUpRequest(
    val username: String,
    val password: String,
    val accountType: String
)
