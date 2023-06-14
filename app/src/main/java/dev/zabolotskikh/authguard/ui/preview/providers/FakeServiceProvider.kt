package dev.zabolotskikh.authguard.ui.preview.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.zabolotskikh.authguard.domain.model.Service

class FakeServiceProvider : PreviewParameterProvider<Service> {
    override val values: Sequence<Service>
        get() = sequenceOf(
            Service(
                isFavorite = false,
                name = "name of service",
                privateKey = "123",
                currentCode = "123123",
                codeTtl = 15000,
                timeoutTime = 30000
            ),
            Service(
                isFavorite = true,
                name = "name of service",
                privateKey = "123",
                currentCode = "123123",
                codeTtl = 15000,
                timeoutTime = 30000
            ),
            Service(
                isFavorite = false,
                name = "servicenameservicenameservicenameservicenameservicenameservicename",
                privateKey = "123",
                currentCode = "123123",
                codeTtl = 15000,
                timeoutTime = 30000
            ),
            Service(
                isFavorite = true,
                name = "servicenameservicenameservicenameservicenameservicenameservicename",
                privateKey = "123",
                currentCode = "123123",
                codeTtl = 15000,
                timeoutTime = 30000
            ),
        )
}