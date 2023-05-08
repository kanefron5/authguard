package dev.zabolotskikh.authguard

import android.net.Uri
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dev.zabolotskikh.authguard.domain.model.BarcodeService

@ExperimentalGetImage
class BarcodeAnalyser(
    val callback: (BarcodeService) -> Unit
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val options =
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()

        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image).addOnSuccessListener { barcodes ->
                for (barcode: Barcode in barcodes) {
                    barcode.rawValue?.apply {
                        if (startsWith("otpauth://totp/")) {
                            val uri = Uri.parse(this)
                            val alias = uri.path?.removePrefix("/") ?: return@apply
                            val secret = uri.getQueryParameter("secret") ?: return@apply
                            val issuer = uri.getQueryParameter("issuer") ?: return@apply
                            callback(BarcodeService(alias, secret, issuer))
                        }
                    }
                }
            }
        }
        imageProxy.close()
    }
}