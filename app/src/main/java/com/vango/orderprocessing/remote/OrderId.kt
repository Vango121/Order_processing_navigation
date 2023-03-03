package com.vango.orderprocessing.remote

data class OrderId(val list: List<CartItemId>, val orderId: Int, val status: Int)