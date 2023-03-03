package com.vango.orderprocessing.ui.products

import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.MedicinesItemBinding
import com.vango.orderprocessing.remote.Medicine
import com.vango.orderprocessing.utils.BaseAdapter

class ProductsAdapter(list: List<Medicine>, val onClick: (Medicine) -> Unit) : BaseAdapter<MedicinesItemBinding, Medicine>(list) {

    override val layoutId: Int = R.layout.medicines_item

    override fun bind(binding: MedicinesItemBinding, item: Medicine) {
        binding.apply {
            medicine = item
            root.setOnClickListener {
                onClick(item)
            }
            executePendingBindings()
        }
    }
}