package com.vango.orderprocessing.ui.pharmaceutist.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.vango.orderprocessing.remote.CartItem
import com.vango.orderprocessing.remote.DataRepository
import com.vango.orderprocessing.remote.Medicine
import com.vango.orderprocessing.remote.QrObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val dataRepository: DataRepository) : ViewModel() {

    val order: MutableLiveData<List<CartItem?>> = MutableLiveData(emptyList())
    val totalPrice: MutableLiveData<Double> = MutableLiveData(0.0)
    private var products: List<Medicine>? = emptyList()

    init {
        viewModelScope.launch {
            products = dataRepository.getAllMedicines()
        }
    }

    fun getOrder(scannedValue: String) {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<QrObject> = moshi.adapter(QrObject::class.java)
        val list = jsonAdapter.fromJson(scannedValue)
        list?.let {
            order.value = mapScannedToOrders(it)
            totalPrice.value = calculateTotalPrice()
        }
    }

    private fun mapScannedToOrders(qrObject: QrObject): List<CartItem?>? =
       products?.filter { medicine -> qrObject.cart.any { it.itemId == medicine.id } }?.map { medicine ->
           qrObject.cart.find { medicine.id == it.itemId }?.quantity?.let { CartItem(medicine, it) } }

    private fun calculateTotalPrice() =
        order.value?.sumOf { it?.quantity!! * it.medicine.price }

    fun clearScanning() {
        totalPrice.postValue(0.0)
        order.postValue(emptyList())
    }

}