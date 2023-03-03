package com.vango.orderprocessing.utils

fun <T: Any>List<T>?.isEmptyOrNull() = this?.let {
    it.isEmpty()
} ?: run {
    null
}