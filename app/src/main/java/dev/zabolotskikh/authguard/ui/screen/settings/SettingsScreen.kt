@file:OptIn(ExperimentalMaterial3Api::class)

package dev.zabolotskikh.authguard.ui.screen.settings

import android.util.Log
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
import dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.PasscodeSetup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Objects
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
                    if (navController.previousBackStackEntry != null) navController.popBackStack()
                    else onNavigateBack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "actionIconContentDescription",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            })
    }, content = { paddingValues ->
        navController.currentBackStackEntryFlow.collectAsEffect(comparator = { o1, o2 ->
            if (o1.destination.route == o2.destination.route) 0
            else -1
        }) {
            it.destination.route?.apply {
                val preferenceSection = toPreferenceSection()
                viewModel.onEvent(SettingsEvent.ChangeSection(preferenceSection))
            }
        }

        viewModel.state.collectAsEffect(comparator = { o1, o2 ->
            o1.currentSection().compareTo(o2.currentSection())
        }) {
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
            composable(PreferenceSection.PasscodeSetup()) {
                PasscodeSetup(
                    paddingValues = paddingValues,
                    state = state,
                    onEvent = viewModel::onEvent,
                    onDismiss = navController::popBackStack
                )
            }
        }
    })
}

@Composable
private fun <T> Flow<T>.collectAsEffect(
    context: CoroutineContext = EmptyCoroutineContext,
    comparator: Comparator<T>? = null,
    block: (T) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        distinctUntilChanged { old, new ->
            comparator?.compare(old, new) == 0
        }.onEach {
            block.invoke(it)
        }.flowOn(context).launchIn(this)
    }
}