package com.example.pulsewearos.presentation

import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.SampleDataPoint
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DeltaDataType
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.getCapabilities
import androidx.health.services.client.unregisterMeasureCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.tasks.await


private const val TAG = "WATCHMAIN"

class HealthServicesManager(context: Context) {
    private val measureClient = HealthServices.getClient(context).measureClient

    suspend fun hasHeartRateCapability() = runCatching {
        val capabilities = measureClient.getCapabilities()
        val hasCapability = (DataType.HEART_RATE_BPM in capabilities.supportedDataTypesMeasure)
        println("💡 Heart rate capability check: $hasCapability")
        hasCapability
    }.getOrDefault(false)

    @ExperimentalCoroutinesApi
    fun heartRateMeasureFlow(): Flow<MeasureMessage> = callbackFlow {
        val callback = object : MeasureCallback {
            override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {
                println("📱 Availability changed for $dataType: $availability")
                if (availability is DataTypeAvailability) {
                    trySendBlocking(MeasureMessage.MeasureAvailability(availability))
                }
            }

            override fun onDataReceived(data: DataPointContainer) {
                try {
                    val heartRateBpm = data.getData(DataType.HEART_RATE_BPM)
                    val heartRate = heartRateBpm.first().value.toInt()
                    println("💓 Received heart rate: $heartRate")
                    trySendBlocking(MeasureMessage.MeasureData(heartRateBpm))
                    runBlocking {
                        println("📤 Attempting to send heart rate to phone...")
                        sendHeartRateToPhone(heartRate)
                    }
                } catch (e: Exception) {
                    println("❌ Error processing heart rate data: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        println("⌛ Registering for heart rate data...")
        measureClient.registerMeasureCallback(DataType.HEART_RATE_BPM, callback)

        awaitClose {
            println("👋 Unregistering heart rate callback")
            runBlocking {
                measureClient.unregisterMeasureCallback(DataType.HEART_RATE_BPM, callback)
            }
        }
    }

    private val dataClient: DataClient = Wearable.getDataClient(context)

    suspend fun sendHeartRateToPhone(heartRate: Int) {
        try {
            val putDataMapRequest = PutDataMapRequest.create("/heartrate").apply {
                dataMap.putInt("heartrate", heartRate)
            }
            val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()
            println("📝 Created data request for heart rate: $heartRate")
            val dataItem = dataClient.putDataItem(putDataRequest).await()
            println("✅ Successfully sent heart rate to phone: $dataItem")
        } catch (exception: Exception) {
            println("❌ Failed to send heart rate data to phone: ${exception.message}")
            exception.printStackTrace()
        }
    }
}

sealed class MeasureMessage {
    class MeasureAvailability(val availability: DataTypeAvailability) : MeasureMessage()
    class MeasureData(val data: List<SampleDataPoint<Double>>) : MeasureMessage()
}