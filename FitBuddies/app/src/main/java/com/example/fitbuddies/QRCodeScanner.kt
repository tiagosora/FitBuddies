package com.example.fitbuddies.qr

import android.Manifest
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.*
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

/**
 * Composable que solicita permissão da câmera e exibe o scanner caso permitido.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRCodeScannerWithPermission(
    modifier: Modifier = Modifier,
    onQrCodeScanned: (String) -> Unit
) {
    // Usamos a biblioteca Accompanist para gerenciar permissões em Compose
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    when {
        cameraPermissionState.status.isGranted -> {
            // Se a permissão já foi concedida, exibe o scanner
            QRCodeScannerScreen(modifier, onQrCodeScanned)
        }
        cameraPermissionState.status.shouldShowRationale ||
                !cameraPermissionState.status.isGranted -> {
            // Se não foi concedida ou usuário negou, tentamos solicitar novamente
            LaunchedEffect(Unit) {
                cameraPermissionState.launchPermissionRequest()
            }
            // Aqui você poderia mostrar uma tela explicando por que a permissão é necessária
        }
    }
}

/**
 * Composable que exibe a preview da câmera e faz a análise dos frames para detectar QR code.
 */
@Composable
fun QRCodeScannerScreen(
    modifier: Modifier = Modifier,
    onQrCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        update = { previewView ->
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                QrCodeAnalyzer(onQrCodeScanned)
                            )
                        }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalyzer
                    )
                } catch (e: Exception) {
                    Log.e("QRScanner", "Use case binding failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}

/**
 * Classe que analisará cada frame para detectar QR code usando ZXing.
 */
class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            // Converte o YUV para um array de bytes
            val buffer = mediaImage.planes[0].buffer
            val bytes = buffer.toByteArray()
            val width = mediaImage.width
            val height = mediaImage.height

            // Cria a fonte luminosa para o ZXing
            val source = PlanarYUVLuminanceSource(
                bytes,
                width,
                height,
                0,
                0,
                width,
                height,
                false
            )
            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
            try {
                // Tenta decodificar
                val result = MultiFormatReader().decode(binaryBmp)
                onQrCodeScanned(result.text)
            } catch (_: NotFoundException) {
                // Se não encontrar, não faz nada
            } finally {
                imageProxy.close()
            }
        } else {
            imageProxy.close()
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind() // Garante que o ponteiro do buffer está no início
        val data = ByteArray(remaining())
        get(data)
        return data
    }
}
