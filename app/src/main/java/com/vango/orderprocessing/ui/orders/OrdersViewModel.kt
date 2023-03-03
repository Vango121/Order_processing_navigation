package com.vango.orderprocessing.ui.orders

import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.vango.orderprocessing.auth.UserService
import com.vango.orderprocessing.remote.*
import com.vango.orderprocessing.utils.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val userService: UserService
) :
    ViewModel() {

    val orders: MutableLiveData<List<Order>> = MutableLiveData()
    private val ordersList: MutableList<Order> = mutableListOf()
    private var products: List<Medicine> = emptyList()
    private var cartList: List<OrderId> = emptyList()
    private var apiJob: Job? = null

    init {
        viewModelScope.launch {
            products = dataRepository.getAllMedicines() ?: emptyList()
        }
    }

    private fun mapScannedToOrders(cartList: List<CartItemId>): List<CartItem?> =
        products.filter { medicine -> cartList.any { it.itemId == medicine.id } }.map { medicine ->
            cartList.find { medicine.id == it.itemId }?.quantity?.let { CartItem(medicine, it) }
        }

    private fun generateJsonCart(order: Order): String {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<QrObject> = moshi.adapter(QrObject::class.java)
        return jsonAdapter.toJson(cartItemToQr(order.list))
    }

    fun generateQrCodeBitmap(order: Order): Bitmap =
        QRGEncoder(generateJsonCart(order), null, QRGContents.Type.TEXT, 512).bitmap

    private fun cartItemToQr(list: List<CartItem?>): QrObject {
        val cart = list.mapNotNull {
            it?.medicine?.id?.let { it1 -> CartItemId(it1, it.quantity) }
        }
        return QrObject(userService.userId, cart)
    }

    fun subscribeData() {
        apiJob = viewModelScope.launch {
            while (true) {
                ordersList.clear()
                cartList = if(isClient()) {
                    dataRepository.getOrders() ?: emptyList()
                } else {
                    dataRepository.getAllOrders() ?: emptyList()
                }
                cartList.forEach {
                    ordersList.add(Order(mapScannedToOrders(it.list), orderId = it.orderId, status = it.status))
                }
                orders.postValue(ordersList)
                delay(Const.REFRESH_DATA_DELAY)
            }
        }
    }

    fun unSubscribe() {
        apiJob?.cancel()
        apiJob = null
    }

    fun isClient(): Boolean =
        userService.accountType == Const.CLIENT

    fun changeOrderStatus(order: Order): Boolean {
        var isOrderStatusChanged = false
        if(order.status < Const.ORDER_COMPLETED) {
            viewModelScope.launch {
                dataRepository.changeOrderStatus(order.orderId.toString())
            }
        } else {
            isOrderStatusChanged = true
        }
        return isOrderStatusChanged
    }

}