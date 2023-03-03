package com.vango.orderprocessing.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.FragmentOrdersBinding
import com.vango.orderprocessing.remote.Order
import com.vango.orderprocessing.ui.BaseFragment
import com.vango.orderprocessing.ui.FragmentType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment() {

    override var type = FragmentType.ORDERS
    private lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@OrdersFragment.viewModel
            ordersAdapter = OrdersAdapter(emptyList(), recyclerViewOnClick, recyclerViewLongCLick)
        }
        binding.titleTextView.text =
            if (viewModel.isClient()) getString(R.string.your_orders) else getString(R.string.all_orders)

        return binding.root
    }

    private val recyclerViewOnClick: (Order) -> Unit = { order ->
        if (viewModel.isClient()) {
            createQrDialog(viewModel.generateQrCodeBitmap(order))
        } else {
            findNavController().navigate(
                OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(
                    order
                )
            )
        }
    }

    private val recyclerViewLongCLick: (Order) -> Unit = { order ->
        if (!viewModel.isClient()) {
            val isOrderStatusChanged = viewModel.changeOrderStatus(order)
            Toast.makeText(
                requireContext(),
                if (isOrderStatusChanged) getString(R.string.status_changed) else getString(R.string.order_already_completed),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            findNavController().navigate(
                OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(
                    order
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribeData()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unSubscribe()
    }

}