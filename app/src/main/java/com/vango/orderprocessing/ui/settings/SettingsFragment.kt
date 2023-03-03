package com.vango.orderprocessing.ui.settings

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.vango.orderprocessing.MainActivity
import com.vango.orderprocessing.R
import com.vango.orderprocessing.databinding.FragmentSettingsBinding
import com.vango.orderprocessing.ui.BaseFragment
import com.vango.orderprocessing.ui.FragmentType
import com.vango.orderprocessing.ui.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    override var type = FragmentType.SETTINGS
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel.logOutEvent.observe(viewLifecycleOwner) {
            openMainActivity()
        }

        binding.logOut.setOnClickListener {
            viewModel.logOut()
        }
        return binding.root
    }

    fun openMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}