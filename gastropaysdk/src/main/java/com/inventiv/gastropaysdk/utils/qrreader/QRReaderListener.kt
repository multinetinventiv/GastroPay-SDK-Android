package com.inventiv.gastropaysdk.utils.qrreader

import com.google.mlkit.vision.barcode.Barcode

internal interface QRReaderListener {
    fun onRead(barcode: Barcode, barcodes: List<Barcode>)
    fun onError(exception: Exception)
}