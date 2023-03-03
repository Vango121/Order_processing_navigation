package com.vango.orderprocessing.ui.pharmaceutist.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.FragmentDashboardPharmeceutistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardPharmeceutistFragment : Fragment() {


    private val viewModel: DashboardPharmeceutistViewModel by viewModels()
    private lateinit var binding: FragmentDashboardPharmeceutistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardPharmeceutistBinding.inflate(inflater, container, false)
        viewModel.numberOfProducts.observe(viewLifecycleOwner) {
            binding.numberOfProductsTextView.text = getString(R.string.number_of_products, it)
        }
        viewModel.numberOfOrders.observe(viewLifecycleOwner) {
            binding.numberOfOrdersTextView.text = getString(R.string.number_of_orders, it)
        }
        return binding.root
    }

}