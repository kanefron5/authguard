package dev.zabolotskikh.passlock.ui.activity.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.passlock.R
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity
import dev.zabolotskikh.passlock.ui.activity.PasscodeEvent
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.BLOCKED
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult.REJECTED
import dev.zabolotskikh.passlock.ui.activity.PasscodeState
import dev.zabolotskikh.passlock.ui.preview.providers.FakePasscodeStateProvider
import java.text.SimpleDateFormat
import java.util.Locale

private fun Long.toFormattedDate(): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(this)
}

@Composable
internal fun EnterPasscodeScreen(
    modifier: Modifier = Modifier,
    options: PasscodeActivity.PasscodeAction.EnterPasscode,
    state: PasscodeState = PasscodeState(),
    onEvent: (PasscodeEvent) -> Unit = {}
) {

    PasscodeButtons(modifier = modifier.fillMaxSize(), title = {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            text = stringResource(id = R.string.passcode_enter),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (state.passcodeCheckStatus == BLOCKED) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = stringResource(
                    id = R.string.passcode_limit_reached, state.isBlockedUntil.toFormattedDate()
                ),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        } else if (state.passcodeCheckStatus != PasscodeResult.SUCCEED) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = if (state.passcodeCheckStatus == REJECTED) stringResource(
                    id = R.string.passcode_rejected, state.remainingAttemptsCount
                ) else stringResource(
                    id = R.string.passcode_attempts_remaining, state.remainingAttemptsCount
                ),
                fontSize = 16.sp,
                color = if (state.passcodeCheckStatus == REJECTED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }, onSubmit = {
        onEvent(PasscodeEvent.EnterPasscode(it))
    })
}

@Composable
@Preview(showSystemUi = true, device = PIXEL_4)
private fun EnterPasscodeScreenPreview(
    @PreviewParameter(FakePasscodeStateProvider::class) state: PasscodeState
) {
    EnterPasscodeScreen(
        options = PasscodeActivity.PasscodeAction.EnterPasscode(), state = state
    )
}