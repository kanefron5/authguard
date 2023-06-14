package dev.zabolotskikh.authguard.ui.screen.services.components

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.ui.preview.providers.FakeServiceStateProvider
import dev.zabolotskikh.authguard.ui.screen.services.ServiceEvent
import dev.zabolotskikh.authguard.ui.screen.services.ServiceState

@Composable
@ExperimentalGetImage
fun ServicesList(
    state: ServiceState,
    onEvent: (ServiceEvent) -> Unit,
    paddingValues: PaddingValues,
) {
    if (state.isAddingService) {
        AddServiceDialog(state, onEvent = onEvent)
    }

    if (state.services.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                LinearProgressIndicator()
            } else {
                Text(
                    text = stringResource(id = R.string.service_list_empty),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Image(
                    painter = painterResource(id = R.drawable.empty_list),
                    contentDescription = stringResource(id = R.string.service_list_empty)
                )
                Button(
                    onClick = {
                        onEvent(ServiceEvent.ShowDialog)
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(id = R.string.service_list_add_first))
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                ), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.services) { service ->
                ServiceListItem(service = service, state = state, onChangeFavorite = {
                    onEvent(
                        if (service.isFavorite) ServiceEvent.RemoveFromFavorite(service)
                        else ServiceEvent.AddToFavorite(service)
                    )
                }, onDelete = {
                    onEvent(ServiceEvent.DeleteService(service))
                })
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "Preview with list")
@Composable
@ExperimentalGetImage
fun ServicesListPreview1(
    @PreviewParameter(FakeServiceStateProvider::class) serviceState: ServiceState
) {
    ServicesList(
        state = serviceState, onEvent = {}, paddingValues = PaddingValues(16.dp)
    )
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "Preview with loading")
@Composable
@ExperimentalGetImage
fun ServicesListPreview2() {
    ServicesList(
        state = ServiceState(
            isLoading = true,
        ), onEvent = {}, paddingValues = PaddingValues(16.dp)
    )
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "Preview with empty list")
@Composable
@ExperimentalGetImage
fun ServicesListPreview3() {
    ServicesList(
        state = ServiceState(
            isLoading = false,
        ), onEvent = {}, paddingValues = PaddingValues(16.dp)
    )
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "Preview with adding service")
@Composable
@ExperimentalGetImage
fun ServicesListPreview4(
    @PreviewParameter(FakeServiceStateProvider::class) serviceState: ServiceState
) {
    ServicesList(
        state = serviceState.copy(
            isLoading = false,
            isAddingService = true,
        ), onEvent = {}, paddingValues = PaddingValues(16.dp)
    )
}