package com.vango.orderprocessing.ui.dashboard

import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.DashboardMedicineItemBinding
import com.vango.orderprocessing.remote.Medicine
import com.vango.orderprocessing.utils.BaseAdapter

class MedicinesAdapter(list: List<Medicine>,  val onClick: (Boolean) -> Unit) : BaseAdapter<DashboardMedicineItemBinding, Medicine>(list) {

    override val layoutId: Int = R.layout.dashboard_medicine_item

    override fun bind(binding: DashboardMedicineItemBinding, item: Medicine) {
        binding.apply {
            medicine = item
            root.setOnClickListener {
                onClick(item.onPrescription)
            }
            executePendingBindings()
        }
    }


}