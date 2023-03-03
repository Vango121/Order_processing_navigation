package com.vango.orderprocessing.remote

data class QrObject(val userId: String?, val cart: List<CartItemId>)