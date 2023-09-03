package dev.zabolotskikh.auth.ui.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import dev.zabolotskikh.authguard.domain.model.UserAccount

@Composable
fun rememberAuth(): UserAccount? {
    val isPreview = LocalInspectionMode.current
    if (isPreview) return null

    val viewModel = hiltViewModel<ProviderViewModel>()
    val state by viewModel.state.collectAsState()
    return state?.userAccount
}