package dev.zabolotskikh.passlock.ui.activity.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity.PasscodeAction.EnterPasscode
import dev.zabolotskikh.passlock.ui.activity.PasscodeEvent
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.BLOCKED
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.REJECTED
import dev.zabolotskikh.passlock.ui.activity.PasscodeState

@Composable
internal fun DeletePasscodeScreen(
    modifier: Modifier = Modifier,
    state: PasscodeState = PasscodeState(),
    onEvent: (PasscodeEvent) -> Unit = {}
) {
    when (state.passcodeCheckStatus) {
        null, BLOCKED, REJECTED -> EnterPasscodeScreen(
            options = EnterPasscode(), state = state, modifier = modifier, onEvent = onEvent
        )

        PasscodeResult.SUCCEED -> onEvent(PasscodeEvent.DeletePasscode)

        else -> {}
    }
}