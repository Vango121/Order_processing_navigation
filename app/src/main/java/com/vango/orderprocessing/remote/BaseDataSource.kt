package com.vango.orderprocessing.remote

import retrofit2.Response
import timber.log.Timber

/**
 * Base abstract class for DataSource
 */
abstract class BaseDataSource {

    /**
     * Function let you handle Retrofit response
     */
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): T? {
        Timber.d(call.toString())
        try {
            Timber.d("Block try/catch started")
            val response = call.invoke()
            Timber.d( "Success?:%s", response.isSuccessful.toString())
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Timber.d(response.body().toString())
                    return body
                }
            }
            Timber.e("error fetching api ${response.code()} ${response.message()}")
            return null
        } catch (e: Exception) {
            Timber.e("error fetching api ${e.message}}")
            return null
        }
    }
}