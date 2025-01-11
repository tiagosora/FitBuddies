package com.example.fitbuddies.qr

import android.Manifest
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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
    val coroutineScope = rememberCoroutineScope()

    // Lembra (memoiza) o ProcessCameraProvider
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    // Caso algum QR seja detectado, guardamos em scannedValue
    var scannedValue by remember { mutableStateOf<String?>(null) }

    // Se `scannedValue` não for nulo, chamamos o callback e podemos encerrar a tela
    scannedValue?.let { qrCode ->
        // Para evitar loop, definimos nulo antes de enviar
        scannedValue = null
        onQrCodeScanned(qrCode)
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { androidContext ->
                // Cria a View que exibirá o preview
                val previewView = androidx.camera.view.PreviewView(androidContext)
                previewView
            },
            update = { previewView ->
                val cameraProvider = cameraProviderFuture.get()

                // Monta o uso de Preview
                val preview = Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                // Monta o uso de Análise (ImageAnalysis)
                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(android.util.Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer { result ->
                        // Quando um QR code for detectado, atualizamos scannedValue
                        coroutineScope.launch(Dispatchers.Main) {
                            scannedValue = result
                        }
                    }
                )

                // Seleciona a câmera traseira
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind para evitar conflitos
                    cameraProvider.unbindAll()
                    // Associa Preview e ImageAnalysis ao ciclo de vida
                    cameraProvider.bindToLifecycle(
                        context as androidx.lifecycle.LifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            }
        )
    }
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
