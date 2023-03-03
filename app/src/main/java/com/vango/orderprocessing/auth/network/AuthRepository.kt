package com.vango.orderprocessing.auth.network

import com.vango.orderprocessing.auth.models.AuthResult

interface AuthRepository {
    suspend fun signUp(username: String, password: String, accountType: String): AuthResult<Unit>
    suspend fun signIn(username: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}