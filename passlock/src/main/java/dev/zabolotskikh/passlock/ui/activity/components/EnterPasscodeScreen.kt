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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.passlock.R
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity
import dev.zabolotskikh.passlock.ui.activity.PasscodeEvent
import dev.zabolotskikh.passlock.ui.activity.PasscodeState

@Composable
internal fun EnterPasscodeScreen(
    modifier: Modifier = Modifier,
    options: PasscodeActivity.PasscodeAction.EnterPasscode,
    state: PasscodeState = PasscodeState(),
    onEvent: (PasscodeEvent) -> Unit = {}
) {
    PasscodeButtons(
        modifier = modifier.fillMaxSize(),
        title = {
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
            if (state.isLimitReached) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    text = stringResource(R.string.passcode_limit_reached),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            if (state.isRejected) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    text = stringResource(R.string.passcode_rejected),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        },
        onSubmit = {
            onEvent(PasscodeEvent.EnterPasscode(it, options.maxAttemptCount))
        }
    )
}