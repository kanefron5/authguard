package dev.zabolotskikh.authguard.ui.preview.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class FakeLabelProvider: PreviewParameterProvider<Pair<String, Boolean>> {
    override val values: Sequence<Pair<String, Boolean>>
        get() = sequenceOf(
            "Я понимаю, что ..." to false,
            "Я понимаю, что ..." to true,
            "Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что" to false,
            "Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что" to true,
            "Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что" to false,
            "Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что Я понимаю, что" to true,
        )
}