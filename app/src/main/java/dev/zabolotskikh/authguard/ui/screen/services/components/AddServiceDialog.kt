package dev.zabolotskikh.authguard.ui.screen.services.components

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.domain.model.GenerationMethod
import dev.zabolotskikh.authguard.ui.screen.services.ServiceEvent
import dev.zabolotskikh.authguard.ui.screen.services.ServiceState

@Composable
@ExperimentalGetImage
fun AddServiceDialog(
    state: ServiceState, onEvent: (ServiceEvent) -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onEvent(ServiceEvent.HideDialog) },
        title = { Text(text = stringResource(id = R.string.add_service_dialog_title)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                var isManualModeSelected by rememberSaveable { mutableStateOf(true) }

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    isManualModeSelected = !isManualModeSelected
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isManualModeSelected) Icons.Outlined.QrCode else Icons.Outlined.Edit,
                            contentDescription = "icon"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = if (isManualModeSelected) stringResource(id = R.string.add_service_manual)
                            else stringResource(id = R.string.add_service_manual)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (isManualModeSelected) {
                    TextField(value = state.name,
                        onValueChange = { onEvent(ServiceEvent.SetName(it)) },
                        placeholder = {
                            Text(text = stringResource(id = R.string.add_service_dialog_name))
                        })
                    TextField(value = state.privateKey,
                        onValueChange = { onEvent(ServiceEvent.SetPrivateKey(it)) },
                        placeholder = {
                            Text(text = stringResource(id = R.string.add_service_dialog_key))
                        })
                    if (state.isBadSecret && state.privateKey.isNotEmpty()) {
                        Text(
                            text = stringResource(id = R.string.add_service_invalid_key),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    MethodSelector(GenerationMethod.values().asList(),
                        GenerationMethod.TIME,
                        onSelectionChanged = {
                            onEvent(ServiceEvent.SetMethod(it))
                        })
                } else {
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                    ) {
                        BarcodeScanner(onSuccess = {
                            isManualModeSelected = true
                            onEvent(ServiceEvent.SetMethod(GenerationMethod.TIME))
                            onEvent(ServiceEvent.SetName(it.alias))
                            onEvent(ServiceEvent.SetPrivateKey(it.secret))
                        })
                    }
                }

            }
        },
        confirmButton = {
            Button(
                onClick = { onEvent(ServiceEvent.SaveService) },
                enabled = (state.name.isNotBlank() && state.privateKey.isNotBlank() && !state.isBadSecret)
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(ServiceEvent.HideDialog) }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier
    )
}