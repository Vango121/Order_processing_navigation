package com.vango.orderprocessing.remote

import android.os.Parcelable
import com.vango.orderprocessing.utils.ListAdapterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medicine(
    val id: Int,
    val name: String,
    val price: Double,
    val onPrescription: Boolean,
    val description: String,
    val imageURL: String,
    val productStock: Int,
    override var idAdapter: Long = 0
) : ListAdapterItem, Parcelable
