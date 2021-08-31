package com.inventiv.gastropaysdk.utils.qrreader

import androidx.camera.core.CameraSelector
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions

internal data class QRCameraConfiguration(
    var lensFacing: Int = CameraSelector.LENS_FACING_BACK,

    val options: BarcodeScannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()
)