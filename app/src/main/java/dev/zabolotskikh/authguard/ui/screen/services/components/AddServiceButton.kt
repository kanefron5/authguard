package dev.zabolotskikh.authguard.ui.screen.services.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.ui.preview.providers.FakeServiceStateProvider
import dev.zabolotskikh.authguard.ui.screen.services.ServiceEvent
import dev.zabolotskikh.authguard.ui.screen.services.ServiceState

@Composable
fun AddServiceButton(
    state: ServiceState, onEvent: (ServiceEvent) -> Unit
) {

    if (state.services.isNotEmpty()) {
        FloatingActionButton(onClick = {
            onEvent(ServiceEvent.ShowDialog)
        }) {
            Text(stringResource(id = R.string.add_service))
        }
    }
}

@Preview
@Composable
fun AddServiceButtonPreview(
    @PreviewParameter(FakeServiceStateProvider::class) serviceState: ServiceState
) {
    AddServiceButton(state = serviceState, onEvent = {})
}