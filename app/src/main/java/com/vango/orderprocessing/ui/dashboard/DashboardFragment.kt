package com.vango.orderprocessing.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.FragmentDashboardBinding
import com.vango.orderprocessing.ui.BaseFragment
import com.vango.orderprocessing.ui.FragmentType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment: BaseFragment() {

    override var type = FragmentType.DASHBOARD

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@DashboardFragment.viewModel
            prescriptionAdapter = MedicinesAdapter(emptyList(), recyclerViewOnClick)
            nonPrescriptionAdapter = MedicinesAdapter(emptyList(), recyclerViewOnClick)
        }
        viewModel.fetchDataFromApi()
        return binding.root
    }

    private val recyclerViewOnClick: (Boolean) -> Unit = { onPrescription ->
        findNavController().navigate(R.id.action_dashboardFragment_to_productListFragment,
        bundleOf("onPrescription" to onPrescription))
    }

}