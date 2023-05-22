@file:OptIn(ExperimentalMaterial3Api::class)

package dev.zabolotskikh.authguard.ui.screen.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.zabolotskikh.authguard.ui.screen.settings.sections.main.Preferences
import dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.PasscodePreferences

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = state.currentSection.title)) },
            navigationIcon = {
                IconButton(onClick = { viewModel.onNavigateBack(onNavigateBack) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "actionIconContentDescription",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            })
    }, content = { paddingValues ->

        when (state.currentSection) {
            PreferenceSection.Main -> {
                Preferences(
                    paddingValues = paddingValues, onEvent = viewModel::onEvent
                )
            }

            PreferenceSection.Passcode -> {
                PasscodePreferences(
                    paddingValues = paddingValues, state = state, onEvent = viewModel::onEvent
                )
            }
        }
    })
}

