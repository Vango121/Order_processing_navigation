package com.vango.orderprocessing.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vango.orderprocessing.SharedViewModel
import com.vango.orderprocessing.databinding.FragmentProductListBinding
import com.vango.orderprocessing.remote.Medicine
import com.vango.orderprocessing.ui.BaseFragment
import com.vango.orderprocessing.ui.FragmentType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProductListFragment : BaseFragment() {

    override var type = FragmentType.PRODUCTSLIST

    private lateinit var binding: FragmentProductListBinding
    private val viewModel: ProductListViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = ProductsAdapter(emptyList(), onClickProduct)
            viewModel = this@ProductListFragment.viewModel
        }
        Timber.e("On create view")
        viewModel.fetchDataFromApi()
        viewModel.setPrescriptionFlag(arguments?.getBoolean("onPrescription") ?: true)
        return binding.root
    }

    private val onClickProduct: (Medicine) -> Unit = { medicine ->
//        sharedViewModel.showSnackbar("Added ${medicine.name} to cart")
//        sharedViewModel.addToCart(medicine, 1)
        findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToBlankFragment())
    }

    override fun onResume() {
        Timber.e("on resume")
        super.onResume()
    }

    override fun onStop() {
        Timber.e("on stop")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.e("on destroy")
        super.onDestroy()
    }

}