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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.zabolotskikh.authguard.ui.screen.settings.sections.main.Preferences
import dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.PasscodePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}, navController: NavHostController = rememberNavController()
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = state.currentSection.title)) },
            navigationIcon = {
                IconButton(onClick = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                        state.currentSection.back?.apply {
                            viewModel.onEvent(SettingsEvent.ChangeSection(this))
                        }
                    } else onNavigateBack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "actionIconContentDescription",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            })
    }, content = { paddingValues ->
        navController.currentBackStackEntryFlow.collectAsEffect {
            it.destination.route?.apply {
                val preferenceSection = toPreferenceSection()
                viewModel.onEvent(SettingsEvent.ChangeSection(preferenceSection))
            }
        }

        viewModel.state.collectAsEffect {
            navController.navigate(it.currentSection()) {
                it.currentSection.back?.apply { popUpTo(this()) }
                launchSingleTop = true
            }
        }

        NavHost(
            navController = navController, startDestination = PreferenceSection.Main()
        ) {
            composable(PreferenceSection.Main()) {
                Preferences(paddingValues = paddingValues, onEvent = viewModel::onEvent)
            }
            composable(PreferenceSection.Passcode()) {
                PasscodePreferences(
                    paddingValues = paddingValues, state = state, onEvent = viewModel::onEvent
                )
            }
        }
    })
}

@Composable
private fun <T> Flow<T>.collectAsEffect(
    context: CoroutineContext = EmptyCoroutineContext, block: (T) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onEach(block).flowOn(context).launchIn(this)
    }
}