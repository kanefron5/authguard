package dev.zabolotskikh.passlock.ui.provider

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleEventObserver
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity
import dev.zabolotskikh.passlock.ui.activity.PasscodeResult

@Composable
fun rememberPasscodeEnabled(): Boolean {
    val isPreview = LocalInspectionMode.current
    if (isPreview) return false

    val viewModel = hiltViewModel<ProviderViewModel>()
    val state by viewModel.state.collectAsState()
    return state?.hasPasscode == true
}

@Composable
fun PassLockProvider(
    content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<ProviderViewModel>()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by viewModel.state.collectAsState()
    var isActivityLaunched by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(PasscodeActivity.PasscodeResultContract()) {
        isActivityLaunched = false
        when (it) {
            PasscodeResult.SUCCEED -> viewModel.onEvent(ProviderEvent.OnValidate)
            else -> {}
        }
    }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            viewModel.onEvent(ProviderEvent.ChangeLifecycle(event))
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(true) {
        if (state?.isLocked == true && !isActivityLaunched) {
            isActivityLaunched = true
            launcher.launch(PasscodeActivity.PasscodeAction.EnterPasscode(false))
        }
    }
    if (state?.isLocked == false) content()
    else {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {

        }
    }
}