package dev.zabolotskikh.authguard.ui.screen.settings.sections.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import dev.zabolotskikh.authguard.BuildConfig
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.ui.screen.settings.PreferenceSection
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsEvent
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsState
import dev.zabolotskikh.authguard.ui.screen.settings.sections.main.components.ChangelogDialog
import dev.zabolotskikh.authguard.ui.screen.settings.sections.main.components.ResetConfirmationDialog
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity.PasscodeAction.DeletePasscode
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult
import dev.zabolotskikh.passlock.ui.provider.rememberPasscodeEnabled

@Composable
fun Preferences(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onEvent: (SettingsEvent) -> Unit,
    state: SettingsState = SettingsState()
) {
    var resetConfirmationDialogShowed by rememberSaveable { mutableStateOf(false) }
    var changelogDialogShowed by rememberSaveable { mutableStateOf(false) }

    val hasPasscode = rememberPasscodeEnabled()
    val launcher = rememberLauncherForActivityResult(PasscodeActivity.PasscodeResultContract()) {
        if(it == PasscodeResult.SUCCEED) onEvent(SettingsEvent.ResetData)
    }


    if (resetConfirmationDialogShowed) {
        ResetConfirmationDialog(onDismiss = { resetConfirmationDialogShowed = false }, onConfirm = {
            resetConfirmationDialogShowed = false
            if (!hasPasscode) onEvent(SettingsEvent.ResetData)
            else launcher.launch(DeletePasscode())
        })
    }

    if (changelogDialogShowed) {
        ChangelogDialog(onDismiss = { changelogDialogShowed = false }, state = state)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = paddingValues.calculateBottomPadding(),
                top = paddingValues.calculateTopPadding()
            ), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SettingsGroup(title = {
                Text(text = stringResource(id = R.string.settings_title_data))
            }) {
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.settings_title_passcode))
                }) {
                    onEvent(SettingsEvent.ChangeSection(PreferenceSection.Passcode))
                }
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.settings_reset))
                }) {
                    resetConfirmationDialogShowed = true
                }
            }

            SettingsGroup(title = {
                Text(text = stringResource(id = R.string.settings_title_about))
            }) {
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.settings_app_version))
                }, subtitle = {
                    Text(text = BuildConfig.VERSION_NAME)
                }) {}
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.settings_app_build_number))
                }, subtitle = {
                    Text(text = BuildConfig.VERSION_CODE.toString())
                }) {
                    onEvent(SettingsEvent.BuildNumberClick)
                }
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.settings_changelog))
                }) {
                    changelogDialogShowed = true
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun PreferencesPreview() {
    Preferences(paddingValues = PaddingValues(16.dp), onEvent = {})
}