package dev.zabolotskikh.passlock.ui.activity.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.passlock.R
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity
import dev.zabolotskikh.passlock.ui.activity.PasscodeEvent
import dev.zabolotskikh.passlock.ui.activity.PasscodeState

@Composable
internal fun SetupPasscodeScreen(
    modifier: Modifier = Modifier,
    options: PasscodeActivity.PasscodeAction.SetupPasscode,
    state: PasscodeState = PasscodeState(),
    onEvent: (PasscodeEvent) -> Unit = {}
) {
    var isNotMatch by rememberSaveable { mutableStateOf(false) }

    PasscodeButtons(isCancelButton = true, onCancel = {
        onEvent(PasscodeEvent.Cancel)
    }, modifier = modifier.fillMaxSize(), title = {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = if (state.attemptCount == 0) stringResource(R.string.passcode_enter_new)
            else stringResource(R.string.passcode_reenter_new),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (isNotMatch) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.passcode_not_match),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }, onSubmit = {
        if (!isNotMatch) {
            if (options.count == state.attemptCount + 1) {
                onEvent(PasscodeEvent.SavePasscode)
            } else {
                onEvent(PasscodeEvent.SetPasscode(it))
            }
        }
    }, onEdit = {
        isNotMatch = state.attemptCount != 0 && it.isNotBlank() && state.passcode != it
    })
}