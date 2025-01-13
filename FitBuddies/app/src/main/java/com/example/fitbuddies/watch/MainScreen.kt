package com.example.pulsewearos.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    healthServicesManager: HealthServicesManager
) {
    var hasPermission by remember { mutableStateOf(false) }
    var heartRate by remember { mutableStateOf("Waiting for data...") }
    val coroutineScope = rememberCoroutineScope()

    // Solicitar a permissão
    PermissionRequest {
        println("✅ Permission granted")
        hasPermission = true
    }

    if (hasPermission) {
        LaunchedEffect(Unit) {
            println("🚀 Starting heart rate monitoring...")
            coroutineScope.launch {
                val hasCapability = healthServicesManager.hasHeartRateCapability()
                println("💪 Device has heart rate capability: $hasCapability")

                if (hasCapability) {
                    healthServicesManager.heartRateMeasureFlow().collect { measureMessage ->
                        when (measureMessage) {
                            is MeasureMessage.MeasureData -> {
                                val newHeartRate = measureMessage.data.first().value
                                println("💓 Updating UI with heart rate: $newHeartRate")
                                heartRate = "Heart Rate: $newHeartRate BPM"
                            }
                            is MeasureMessage.MeasureAvailability -> {
                                println("📊 Measure availability changed: ${measureMessage.availability}")
                            }
                        }
                    }
                } else {
                    println("❌ Heart rate monitoring not supported")
                    heartRate = "Heart rate monitoring not supported on this device."
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize() // Ocupa todo o espaço disponível
            .background(color = Color.Black), // Define o fundo preto
        contentAlignment = Alignment.Center // Centraliza o conteúdo
    ) {
        Text(
            text = heartRate,
            color = Color.White // Define a cor do texto como branco
        )
    }
}


