@file:OptIn(ExperimentalMaterial3Api::class)

package dev.zabolotskikh.authguard.ui.screen.services

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.ui.Screen
import dev.zabolotskikh.authguard.ui.preview.providers.FakeServiceStateProvider
import dev.zabolotskikh.authguard.ui.screen.services.components.AddServiceButton
import dev.zabolotskikh.authguard.ui.screen.services.components.AppBarTitle
import dev.zabolotskikh.authguard.ui.screen.services.components.ServicesList

@Composable
@ExperimentalGetImage
fun ServiceScreen(
    onNavigate: (screen: Screen, clear: Boolean) -> Unit = { _, _ -> }
) {
    val viewModel = hiltViewModel<ServiceViewModel>()
    val state by viewModel.state.collectAsState()

    ServiceScreenContent(state, viewModel::onEvent, onNavigate)
}

@Composable
@ExperimentalGetImage
private fun ServiceScreenContent(
    state: ServiceState = ServiceState(),
    onEvent: (ServiceEvent) -> Unit = {},
    onNavigate: (screen: Screen, clear: Boolean) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { AppBarTitle() },
                actions = {
                    if (state.services.isNotEmpty()) {
                        IconButton(onClick = {
                            onEvent(
                                if (state.isPrivateMode) ServiceEvent.PrivateModeOff
                                else ServiceEvent.PrivateModeOn
                            )
                        }) {
                            Icon(
                                imageVector = if (state.isPrivateMode) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "actionIconContentDescription",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(onClick = { onNavigate(Screen.Settings, false) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "actionIconContentDescription",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                })
        },
        content = { paddingValues ->
            ServicesList(state, onEvent, paddingValues)
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { AddServiceButton(state, onEvent) },
    )
}

@Preview(showSystemUi = true, showBackground = true, device = PIXEL_4)
@Composable
@ExperimentalGetImage
private fun Preview() {
    ServiceScreenContent()
}

@Preview(showSystemUi = true, showBackground = true, device = PIXEL_4)
@Composable
@ExperimentalGetImage
private fun PreviewNotEmpty(
    @PreviewParameter(FakeServiceStateProvider::class) state: ServiceState
) {
    ServiceScreenContent(state = state)
}