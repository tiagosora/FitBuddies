package com.example.fitbuddies.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

data class RouteUiState(
    val hasLocationPermission: Boolean = false,
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val currentLocation: LatLng? = null,
    val routePoints: List<LatLng> = emptyList(),
    val totalDistanceMeters: Float = 0f // TODO: Mandar Para a API
)

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                updateLocation(location)
            }
        }
    }

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setMinUpdateIntervalMillis(5000)
        .build()

    fun onPermissionGranted() {
        _uiState.value = _uiState.value.copy(hasLocationPermission = true)
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        } catch (e: SecurityException) {
            // Handle permission denial
        }
    }

    private fun updateLocation(location: Location) {
        val newLocation = LatLng(location.latitude, location.longitude)
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.isTracking && !currentState.isPaused) {
                val newDistance = if (currentState.routePoints.isNotEmpty()) {
                    val lastPoint = currentState.routePoints.last()
                    val results = FloatArray(1)
                    Location.distanceBetween(
                        lastPoint.latitude, lastPoint.longitude,
                        newLocation.latitude, newLocation.longitude,
                        results
                    )
                    currentState.totalDistanceMeters + results[0]
                } else {
                    currentState.totalDistanceMeters
                }

                _uiState.value = currentState.copy(
                    currentLocation = newLocation,
                    routePoints = currentState.routePoints + newLocation,
                    totalDistanceMeters = newDistance
                )
            } else {
                _uiState.value = currentState.copy(currentLocation = newLocation)
            }
        }
    }

    fun startRoute() {
        _uiState.value = _uiState.value.copy(
            isTracking = true,
            isPaused = false,
            routePoints = emptyList(),
            totalDistanceMeters = 0f
        )
    }

    fun pauseRoute() {
        _uiState.value = _uiState.value.copy(isPaused = true)
    }

    fun resumeRoute() {
        _uiState.value = _uiState.value.copy(isPaused = false)
    }

    fun endRoute() {
        _uiState.value = _uiState.value.copy(
            isTracking = false,
            isPaused = false
        )
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}