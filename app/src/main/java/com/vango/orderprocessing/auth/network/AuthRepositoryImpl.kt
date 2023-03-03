package com.vango.orderprocessing.auth.network

import com.vango.orderprocessing.utils.Const
import com.vango.orderprocessing.auth.EncryptedPrefs
import com.vango.orderprocessing.auth.UserService
import com.vango.orderprocessing.auth.models.AuthResult
import com.vango.orderprocessing.auth.models.LoginRequest
import com.vango.orderprocessing.auth.models.SignUpRequest
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService,
    private val encryptedPrefs: EncryptedPrefs,
    private val userService: UserService
): AuthRepository {

    override suspend fun signUp(
        username: String,
        password: String,
        accountType: String
    ): AuthResult<Unit> {
        return try {
            api.signup(
                request = SignUpRequest(
                    username = username,
                    password = password,
                    accountType = "CLIENT"
                )
            )
            signIn(username, password)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Timber.e("HTTP Unauthorized")
                AuthResult.UnAuthorized()
            } else {
                Timber.e("HTTP Unknown Error")
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Timber.e("error $e")
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.signin(
                request = LoginRequest(
                    username = username,
                    password = password
                )
            )
            encryptedPrefs.putString(Const.SHARED_PREF_JWT_TOKEN, response.token)
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Timber.e("HTTP Unauthorized")
                AuthResult.UnAuthorized()
            } else {
                Timber.e("HTTP Unknown Error")
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Timber.e("error $e")
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = encryptedPrefs.getString(Const.SHARED_PREF_JWT_TOKEN) ?: return AuthResult.UnAuthorized()
            val response = api.authenticate("Bearer $token")
            userService.userId = response.userId
            Timber.e("user id ${response.userId}")
            userService.accountType = response.accountType
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Timber.e("HTTP Unauthorized")
                AuthResult.UnAuthorized()
            } else {
                Timber.e("HTTP Unknown Error")
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Timber.e("error $e")
            AuthResult.UnknownError()
        }
    }
}