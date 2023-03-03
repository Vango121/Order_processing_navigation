package com.vango.orderprocessing.ui.cart

import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.CartItemBinding
import com.vango.orderprocessing.remote.CartItem
import com.vango.orderprocessing.utils.BaseAdapter

class CartAdapter(list: List<CartItem>) : BaseAdapter<CartItemBinding, CartItem>(list) {

    override val layoutId: Int = R.layout.cart_item

    override fun bind(binding: CartItemBinding, item: CartItem) {
        binding.apply {
            cartItem = item
            executePendingBindings()
        }
    }
}