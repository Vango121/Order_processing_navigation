package com.vango.orderprocessing.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import timber.log.Timber

enum class FragmentType {
    NONE, DASHBOARD, CART, ORDERS, SETTINGS, PRODUCTSLIST, SCAN
}

abstract class BaseFragment: Fragment() {
    open var type = FragmentType.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Fragment $type created")
    }

    fun createQrDialog(bitmap: Bitmap) {
        val qrCodeImageView = ImageView(requireContext())
        qrCodeImageView.setImageBitmap(bitmap)

        AlertDialog.Builder(requireContext()).apply {
            setTitle("QR code")
            setView(qrCodeImageView)
        }.show()
    }

}
