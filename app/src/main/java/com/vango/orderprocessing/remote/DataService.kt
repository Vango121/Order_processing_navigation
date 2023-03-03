package com.vango.orderprocessing.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface DataService {

    @GET("medicines")
    suspend fun getAllMedicines( @Header("Authorization") token: String): Response<List<Medicine>>

    @POST("createCart")
    suspend fun createCart(@Header("Authorization") token: String, @Body cartItem: QrObject): Response<Unit>

    @GET("getOrders")
    suspend fun getOrders(@Header("Authorization") token: String, @Query("userId") userId: String): Response<List<OrderId>>

    @GET("getAllOrders")
    suspend fun getAllOrders(@Header("Authorization") token: String): Response<List<OrderId>>

    @GET("changeOrderStatus")
    suspend fun changeOrderStatus(@Header("Authorization") token: String, @Query("orderId") orderId: String): Response<Unit>

}