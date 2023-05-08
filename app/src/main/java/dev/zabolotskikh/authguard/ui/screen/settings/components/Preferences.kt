package dev.zabolotskikh.authguard.ui.screen.settings.components

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
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import dev.zabolotskikh.authguard.BuildConfig
import dev.zabolotskikh.authguard.R

@Composable
fun Preferences(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onResetData: () -> Unit,
    onBuildNumberClick: () -> Unit
) {
    var resetConfirmationDialogShowed by rememberSaveable { mutableStateOf(false) }

    if (resetConfirmationDialogShowed) {
        AlertDialog(onDismissRequest = { resetConfirmationDialogShowed = false },
            title = { Text(text = stringResource(id = R.string.confirm_action)) },
            text = {
                Text(text = stringResource(id = R.string.reset_warning))
            },
            confirmButton = {
                Button(
                    onClick = onResetData, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { resetConfirmationDialogShowed = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            })
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