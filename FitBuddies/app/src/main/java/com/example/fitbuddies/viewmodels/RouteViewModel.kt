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


data class RouteUiState(
    val hasLocationPermission: Boolean = false,
    val isTracking: Boolean = false,
    val currentLocation: LatLng? = null,
    val routePoints: List<LatLng> = emptyList()
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
            _uiState.value = _uiState.value.copy(
                currentLocation = newLocation,
                routePoints = if (_uiState.value.isTracking) {
                    _uiState.value.routePoints + newLocation
                } else {
                    _uiState.value.routePoints
                }
            )
        }
    }

    fun startRoute() {
        _uiState.value = _uiState.value.copy(
            isTracking = true,
            routePoints = emptyList()
        )
    }

    fun endRoute() {
        _uiState.value = _uiState.value.copy(isTracking = false)
        // Aqui você pode implementar a lógica para salvar a rota
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
