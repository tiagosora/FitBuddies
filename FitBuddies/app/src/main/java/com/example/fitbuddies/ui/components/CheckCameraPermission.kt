package com.example.fitbuddies.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Verifica a permissão de câmera. Quando concedida, exibe o [content].
 */
@Composable
fun CheckCameraPermission(content: @Composable () -> Unit) {
    val context = LocalContext.current

    // Estado que controla se a permissão já foi concedida
    var hasPermission by remember { mutableStateOf(false) }

    // Lançador para pedir permissão da Câmera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
            if (!isGranted) {
                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Verifica/solicita a permissão logo que o composable "nasce"
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            hasPermission = true
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Só mostra o conteúdo (scanner de QR code) se a permissão tiver sido concedida
    if (hasPermission) {
        content()
    }
}
