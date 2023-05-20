package dev.zabolotskikh.authguard.ui.screen.settings.components

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import dev.zabolotskikh.authguard.ui.screen.services.ServiceScreen

@Composable
fun Preferences(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onResetData: () -> Unit,
    onBuildNumberClick: () -> Unit
) {
    var resetConfirmationDialogShowed by rememberSaveable { mutableStateOf(false) }

    if (resetConfirmationDialogShowed) {
        ResetConfirmationDialog(
            onDismiss = { resetConfirmationDialogShowed = false },
            onConfirm = onResetData
        )
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
                    onBuildNumberClick()
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun PreferencesPreview() {
    Preferences(
        paddingValues = PaddingValues(16.dp),
        onResetData = {},
        onBuildNumberClick = {}
    )
}