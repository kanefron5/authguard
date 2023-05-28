package dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsMenuLink
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.ui.screen.settings.PreferenceSection
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsEvent
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsState
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity.PasscodeAction.DeletePasscode
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity.PasscodeAction.EditPasscode
import dev.zabolotskikh.passlock.ui.provider.rememberPasscodeEnabled

@Composable
fun PasscodePreferences(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state: SettingsState = SettingsState(),
    onEvent: (SettingsEvent) -> Unit = {},
) {
    val launcher = rememberLauncherForActivityResult(PasscodeActivity.PasscodeResultContract()) {}

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = paddingValues.calculateBottomPadding(),
                top = paddingValues.calculateTopPadding()
            ), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            val isPasscodeEnabled = rememberPasscodeEnabled()
            if (isPasscodeEnabled) {
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.edit_passcode))
                }) {
                    launcher.launch(EditPasscode())
                }
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.delete_passcode))
                }) {
                    launcher.launch(DeletePasscode())
                }
            } else {
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.set_passcode))
                }) {
                    onEvent(SettingsEvent.ChangeSection(PreferenceSection.PasscodeSetup))
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "Without passcode")
@Composable
private fun PasscodePreferencesPreview1() {
    PasscodePreferences(
        paddingValues = PaddingValues(16.dp), state = SettingsState()
    )
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "With passcode")
@Composable
private fun PasscodePreferencesPreview2() {
    PasscodePreferences(
        paddingValues = PaddingValues(16.dp), state = SettingsState(isPasscodeEnabled = true)
    )
}