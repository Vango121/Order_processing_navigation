package com.vango.orderprocessing.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vango.orderprocessing.R
import com.vango.orderprocessing.SharedViewModel
import com.vango.orderprocessing.databinding.FragmentCartBinding
import com.vango.orderprocessing.ui.BaseFragment
import com.vango.orderprocessing.ui.FragmentType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CartFragment : BaseFragment() {
    override var type = FragmentType.CART

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = CartAdapter(listOf())
            viewModel = sharedViewModel
        }

        val swipeHandler = object : SwipeCart(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.cartRecyclerView.adapter as CartAdapter
                val newList = adapter.data.toMutableList()
                val removedItem = newList.removeAt(viewHolder.adapterPosition)
                adapter.updateData(newList)
                sharedViewModel.removeFromCart(removedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.cartRecyclerView)
        sharedViewModel.cart.observe(viewLifecycleOwner) {
            val adapter = binding.cartRecyclerView.adapter as CartAdapter
            it?.let { list ->
                adapter.updateData(list)
            }
        }

        binding.cartButton.setOnClickListener {
            sharedViewModel.createCart()
            sharedViewModel.generateQrCodeBitmap()?.let { bitmap -> createQrDialog(bitmap) }
                ?: run {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.empty_cart),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        return binding.root
    }

}