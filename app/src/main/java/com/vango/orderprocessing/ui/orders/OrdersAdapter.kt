package com.vango.orderprocessing.ui.orders

import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.OrderItemBinding
import com.vango.orderprocessing.remote.Order
import com.vango.orderprocessing.utils.BaseAdapter

class OrdersAdapter(list: List<Order>, val onClick: (Order) -> Unit, val onLongClick: (Order) -> Unit) : BaseAdapter<OrderItemBinding, Order>(list) {
    override val layoutId: Int = R.layout.order_item

    override fun bind(binding: OrderItemBinding, item: Order) {
        binding.apply {
            order = item
            root.setOnClickListener {
                onClick(item)
            }
            root.setOnLongClickListener {
                onLongClick(item)
                true
            }
            executePendingBindings()
        }
    }
}