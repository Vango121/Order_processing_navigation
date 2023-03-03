package com.vango.orderprocessing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vango.orderprocessing.databinding.FragmentBlankBinding
import timber.log.Timber


class BlankFragment : Fragment() {

    lateinit var binding: FragmentBlankBinding
    var bottomSheet: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlankBinding.inflate(layoutInflater, container, false)
        binding.text.setOnClickListener {
            Timber.e("clicked")
            bottomSheet?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet = BottomSheetBehavior.from(binding.bottom.bottomLayout).also {
            it.state = BottomSheetBehavior.STATE_HIDDEN
            it.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, state: Int) {
                    Timber.e("state $state")
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // do nothing
                }
            })
        }
    }

}