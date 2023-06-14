package dev.zabolotskikh.passlock.ui.preview.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult
import dev.zabolotskikh.passlock.ui.activity.PasscodeState

internal class FakePasscodeStateProvider: PreviewParameterProvider<PasscodeState> {
    override val values: Sequence<PasscodeState>
        get() = sequenceOf(
            PasscodeState(),
            PasscodeState(passcodeCheckStatus = PasscodeResult.REJECTED),
            PasscodeState(isBlockedUntil = 1685050000000, passcodeCheckStatus = PasscodeResult.BLOCKED)
        )
}