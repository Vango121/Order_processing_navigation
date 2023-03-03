package com.vango.orderprocessing.remote

import android.os.Parcelable
import com.vango.orderprocessing.utils.ListAdapterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(val medicine: Medicine, var quantity: Int = 0, override var idAdapter: Long = 0): ListAdapterItem, Parcelable