package com.example.pulsewearos.presentation

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequest(
    onPermissionGranted: () -> Unit
) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.BODY_SENSORS)

    LaunchedEffect(key1 = permissionState.status) {
        if (permissionState.status.isGranted) {
            onPermissionGranted()
        } else {
            permissionState.launchPermissionRequest()
        }
    }
}
