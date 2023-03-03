package com.vango.orderprocessing.auth.network

import com.vango.orderprocessing.auth.models.AuthenticateResponse
import com.vango.orderprocessing.auth.models.LoginRequest
import com.vango.orderprocessing.auth.models.SignUpRequest
import com.vango.orderprocessing.auth.models.TokenResponse
import com.vango.orderprocessing.remote.Medicine
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("signin")
    suspend fun signin(
        @Body request: LoginRequest
    ): TokenResponse

    @POST("signup")
    suspend fun signup(
        @Body request: SignUpRequest
    )

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    ): AuthenticateResponse

}