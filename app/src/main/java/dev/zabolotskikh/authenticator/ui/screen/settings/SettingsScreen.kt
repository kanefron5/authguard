@file:OptIn(ExperimentalMaterial3Api::class)

package dev.zabolotskikh.authenticator.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import dev.zabolotskikh.authenticator.BuildConfig
import dev.zabolotskikh.authenticator.R

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val viewModel = hiltViewModel<SettingsViewModel>()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "actionIconContentDescription",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            })
    }, content = { paddingValues ->
        Preferences(
            paddingValues = paddingValues,
            onResetData = viewModel::resetData,
            onBuildNumberClick = viewModel::easterEgg
        )
    })
}

@Composable
fun Preferences(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    onResetData: () -> Unit,
    onBuildNumberClick: () -> Unit
) {
    var resetConfirmationDialogShowed by remember { mutableStateOf(false) }

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