package dev.zabolotskikh.authguard.ui.preview.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.ui.screen.services.ServiceState

class FakeServiceStateProvider : PreviewParameterProvider<ServiceState> {
    override val values: Sequence<ServiceState>
        get() = sequenceOf(
            ServiceState(
                services = listOf(
                    Service(
                        isFavorite = true,
                        name = "name of service",
                        privateKey = "123",
                        currentCode = "123123",
                        codeTtl = 15000,
                        timeoutTime = 30000
                    ),
                    Service(
                        name = "name of service",
                        privateKey = "123",
                        currentCode = "123123",
                        codeTtl = 15000,
                        timeoutTime = 30000
                    ),
                    Service(
                        name = "name of service",
                        privateKey = "123",
                        currentCode = "123123",
                        codeTtl = 15000,
                        timeoutTime = 30000
                    ),
                    Service(
                        name = "name of service",
                        privateKey = "123",
                        currentCode = "123123",
                        codeTtl = 15000,
                        timeoutTime = 30000
                    ),
                )
            )
        )
}