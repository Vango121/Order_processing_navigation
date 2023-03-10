package com.vango.orderprocessing.utils

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<BINDING: ViewDataBinding>(val binder: BINDING): RecyclerView.ViewHolder(binder.root) {
}