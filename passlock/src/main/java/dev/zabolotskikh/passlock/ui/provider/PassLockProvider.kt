package dev.zabolotskikh.passlock.ui.provider

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleEventObserver
import dev.zabolotskikh.passlock.ui.activity.PasscodeActivity

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
        println(it)
        when (it) {
            PasscodeActivity.PasscodeResult.Succeed -> viewModel.onEvent(ProviderEvent.OnValidate)
            PasscodeActivity.PasscodeResult.LimitReached -> {}
            PasscodeActivity.PasscodeResult.Cancelled -> {}
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
            launcher.launch(PasscodeActivity.PasscodeAction.EnterPasscode(5, false))
        }
    }
    if (state?.isLocked == false) content()
}