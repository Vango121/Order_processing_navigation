package com.vango.orderprocessing.remote

import android.os.Parcelable
import com.vango.orderprocessing.R
import com.vango.orderprocessing.utils.ListAdapterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(val list: List<CartItem?>, override var idAdapter: Long = 0, val orderId: Int, val status: Int): ListAdapterItem, Parcelable {
    fun calculateTotalPrice(): Double =
        list.sumOf { it?.quantity?.times(it.medicine.price) ?: 0.0 }

    fun isPrescription() = list.any { it?.medicine?.onPrescription ?: false }

    fun getOrderStatus(): Int {
        return when(status) {
            0 -> R.string.order_placed
            1 -> R.string.order_ready
            else -> R.string.order_completed
        }
    }
}