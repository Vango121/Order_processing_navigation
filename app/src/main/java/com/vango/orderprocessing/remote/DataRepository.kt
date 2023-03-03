package com.vango.orderprocessing.remote


interface DataRepository {
    suspend fun getAllMedicines(): List<Medicine>?

    suspend fun createCart(cartItem: QrObject)

    suspend fun getOrders(): List<OrderId>?

    suspend fun getAllOrders(): List<OrderId>?

    suspend fun changeOrderStatus(orderId: String)
}