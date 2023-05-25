package dev.zabolotskikh.passlock.ui.activity.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity
import dev.zabolotskikh.passlock.ui.activity.PasscodeEvent

@Composable
internal fun EnterPasscodeScreen(
    modifier: Modifier = Modifier,
    options: PasscodeActivity.PasscodeAction.EnterPasscode,
    onEvent: (PasscodeEvent) -> Unit = {}
) {
    PasscodeButtons(
        modifier = modifier.fillMaxSize(),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Введите код: ",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Options: $options",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        },
        onSubmit = {
            onEvent(PasscodeEvent.EnterPasscode(it))
        }
    )
}