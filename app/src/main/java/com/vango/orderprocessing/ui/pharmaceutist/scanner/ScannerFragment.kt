package com.vango.orderprocessing.ui.pharmaceutist.scanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.vango.orderprocessing.databinding.FragmentScannerBinding
import com.vango.orderprocessing.ui.BaseFragment
import com.vango.orderprocessing.ui.FragmentType
import com.vango.orderprocessing.ui.cart.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class ScannerFragment : BaseFragment() {
    override var type = FragmentType.SCAN

    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private val viewModel: ScannerViewModel by viewModels()
    private lateinit var binding: FragmentScannerBinding
    private var scannedValue = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScannerBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = CartAdapter(emptyList())
            viewModel = this@ScannerFragment.viewModel
        }
        setupControls()
        binding.scanButton.setOnClickListener {
            viewModel.clearScanning()
            setupControls()
        }
        return binding.root
    }

    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        binding.cameraSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue
                    activity?.runOnUiThread {
                        viewModel.getOrder(scannedValue)
                        cameraSource.stop()
                    }
                }
            }
        })
    }

}