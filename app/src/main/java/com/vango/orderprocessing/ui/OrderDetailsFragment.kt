package com.vango.orderprocessing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.vango.orderprocessing.databinding.FragmentOrderDetailsBinding
import com.vango.orderprocessing.ui.cart.CartAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment() {

    private val viewModel: OrderDetailsViewModel by viewModels()
    private lateinit var binding: FragmentOrderDetailsBinding
    val args: OrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = CartAdapter(args.order.list.filterNotNull())
        }
        binding.orderRecyclerView
        return binding.root
    }

}