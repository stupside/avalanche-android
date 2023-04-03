package com.example.avalanche.qr

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@ExperimentalGetImage
class AvalancheQrAnalyzer(
    private val scanner: BarcodeScanner,
    private val onAnalyzed: (code: Barcode?) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(proxy: ImageProxy) {

        val mediaImage = proxy.image ?: return

        val image = InputImage.fromMediaImage(mediaImage, proxy.imageInfo.rotationDegrees)

        scanner.process(image).addOnSuccessListener {
            val code = it.firstOrNull()

            onAnalyzed(code)

            proxy.close()
        }
    }
}