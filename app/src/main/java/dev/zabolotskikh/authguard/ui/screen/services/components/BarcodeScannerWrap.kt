@file:OptIn(ExperimentalPermissionsApi::class)

package dev.zabolotskikh.authguard.ui.screen.services.components

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.domain.model.BarcodeService

@Composable
@ExperimentalGetImage
fun BarcodeScannerWrap(
    onSuccess: (BarcodeService) -> Unit = {}
) {
    val cameraPermissions = rememberPermissionState(Manifest.permission.CAMERA)

    if (cameraPermissions.hasPermission) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        ) {
            BarcodeScanner(onSuccess = onSuccess)
        }
    } else {
        Column {
            val currentContext = LocalContext.current

            val openSettings = {
                currentContext.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts(
                        "package", currentContext.packageName, null
                    )
                })
            }
            val requestPermissions = { cameraPermissions.launchPermissionRequest() }

            val action = if (cameraPermissions.shouldShowRationale) openSettings
            else requestPermissions

            val textToShow = stringResource(
                id = if (!cameraPermissions.shouldShowRationale) R.string.camera_permission_first
                else R.string.camera_permission_denied
            )
            val buttonText = stringResource(
                id = if (!cameraPermissions.shouldShowRationale) R.string.camera_permission_btn_first
                else R.string.camera_permission_btn_denied
            )

            Text(textToShow)
            TextButton(onClick = action) {
                Text(buttonText)
            }
        }
    }
}