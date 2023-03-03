package com.vango.orderprocessing

import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.vango.orderprocessing.auth.UserService
import com.vango.orderprocessing.remote.*
import com.vango.orderprocessing.utils.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val userService: UserService,
    private val dataRepository: DataRepository
) : ViewModel() {

    private val cartList: MutableList<CartItem> = mutableListOf()
    private val _cart = MutableLiveData<List<CartItem>?>(null)
    val cart: LiveData<List<CartItem>?> = _cart

    private val _badgeText = MutableLiveData<String>()
    val badgeText: LiveData<String> = _badgeText

    fun addToCart(item: Medicine, quantity: Int) {
        cartList.find { it.medicine.id == item.id }?.let {
            it.quantity += quantity
            _cart.postValue(cartList)
        } ?: run {
            cartList.add(CartItem(item, quantity))
            _cart.postValue(cartList)
        }
    }

    fun removeFromCart(item: CartItem) {
        cartList.remove(item)
        _cart.postValue(cartList)
    }

    private fun cartItemToQr(list: List<CartItem>): QrObject {
        val cart = list.map {
            CartItemId(it.medicine.id, it.quantity)
        }
        return QrObject(userService.userId, cart)
    }

    private fun generateJsonCart(): String {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<QrObject> = moshi.adapter(QrObject::class.java)
        return jsonAdapter.toJson(cartItemToQr(cartList))
    }

    fun generateQrCodeBitmap(): Bitmap? = if (cartList.isNotEmpty()) QRGEncoder(
        generateJsonCart(),
        null,
        QRGContents.Type.TEXT,
        512
    ).bitmap else null

    fun showSnackbar(text: String) {
        _badgeText.postValue(text)
    }

    fun createCart() {
        if (cartList.isNotEmpty()) {
            viewModelScope.launch {
                dataRepository.createCart(cartItemToQr(cartList))
            }
        }
    }

    fun getAccountType(): String = userService.accountType ?: Const.CLIENT
}