package dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsEvent
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsState
import dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.components.SettingsPasscodeCheck

@Composable
fun PasscodeSetup(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state: SettingsState = SettingsState(),
    onEvent: (SettingsEvent) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (state.passcodeSettingsProcess) {
        var isShowError by rememberSaveable { mutableStateOf(false) }

        PasscodeButtons(modifier = Modifier.padding(paddingValues), onEdit = {
            isShowError =
                state.passcodeSettingsAttempt > 0 && it.isNotBlank() && it != state.passcodeSettingsCurrent
        }, onSubmit = {
            onEvent(SettingsEvent.OnEnterPasscode(it))
        }, title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (state.passcodeSettingsAttempt == 0) "Введите код-пароль" else "Подтвердите код-пароль",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            if (isShowError) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Введенные пароли не совпадают",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        })
    } else {
        LaunchedEffect(true) {
            println(state)
        }
        var check1 by rememberSaveable { mutableStateOf(false) }
        var check2 by rememberSaveable { mutableStateOf(false) }
        var check3 by rememberSaveable { mutableStateOf(false) }

        val isChecksCompleted = check1 && check2 && check3

        Column(
            modifier = modifier
                .padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                )
                .fillMaxSize()
                .padding(32.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Предупреждение",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.passcode_settings_info),
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    SettingsPasscodeCheck(
                        title = stringResource(id = R.string.passcode_check_restore),
                        onCheckChange = { check1 = it },
                        isChecked = check1
                    )
                    SettingsPasscodeCheck(
                        title = stringResource(id = R.string.passcode_check_restore_developer),
                        onCheckChange = { check2 = it },
                        isChecked = check2
                    )
                    SettingsPasscodeCheck(
                        title = stringResource(id = R.string.passcode_check_lost_data),
                        onCheckChange = { check3 = it },
                        isChecked = check3
                    )
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                Button(onClick = {
                    onEvent(SettingsEvent.StartPasscodeSetting)
                }, enabled = isChecksCompleted) {
                    Text(text = stringResource(id = R.string.action_continue))
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "Default")
@Composable
private fun SettingsPasscodeInfoPreview1() {
    PasscodeSetup(paddingValues = PaddingValues(16.dp))
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "First attempt")
@Composable
private fun SettingsPasscodeInfoPreview2() {
    PasscodeSetup(
        paddingValues = PaddingValues(16.dp), state = SettingsState(passcodeSettingsProcess = true)
    )
}