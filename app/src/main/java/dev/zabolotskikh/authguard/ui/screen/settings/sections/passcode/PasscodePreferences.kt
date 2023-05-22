package dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode

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
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsEvent
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsState
import dev.zabolotskikh.authguard.ui.screen.settings.sections.main.components.ResetConfirmationDialog

@Composable
fun PasscodePreferences(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state: SettingsState = SettingsState(),
    onEvent: (SettingsEvent) -> Unit = {},
) {
    /*
    SettingsPasscodeInfo(
                    modifier = Modifier.padding(
                        bottom = paddingValues.calculateBottomPadding(),
                        top = paddingValues.calculateTopPadding()
                    )
                )
     */

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = paddingValues.calculateBottomPadding(),
                top = paddingValues.calculateTopPadding()
            ), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            if (state.isPasscodeEnabled) {
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.edit_passcode))
                }) {
                    onEvent(SettingsEvent.SetPasscode)
                }
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.delete_passcode))
                }) {
                    onEvent(SettingsEvent.DeletePasscode)
                }
            } else {
                SettingsMenuLink(title = {
                    Text(text = stringResource(id = R.string.set_passcode))
                }) {
                    onEvent(SettingsEvent.SetPasscode)
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "Without passcode")
@Composable
private fun PasscodePreferencesPreview1() {
    PasscodePreferences(
        paddingValues = PaddingValues(16.dp),
        state = SettingsState()
    )
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true, name = "With passcode")
@Composable
private fun PasscodePreferencesPreview2() {
    PasscodePreferences(
        paddingValues = PaddingValues(16.dp),
        state = SettingsState(isPasscodeEnabled = true)
    )
}