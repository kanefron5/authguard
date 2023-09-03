package dev.zabolotskikh.authguard.ui.screen.settings.sections.main.account

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import dev.zabolotskikh.auth.ui.activity.AuthActivity
import dev.zabolotskikh.auth.ui.preview.provider.FakeUserAccountProvider
import dev.zabolotskikh.auth.ui.provider.rememberAuth
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.domain.model.UserAccount

@Composable
fun AccountSettingsGroup() {
    val viewModel = hiltViewModel<AccountSettingsViewModel>()
    val userAccount = rememberAuth()

    AccountSettingsGroupView(userAccount = userAccount, onEvent = viewModel::onEvent)
}

@Composable
private fun AccountSettingsGroupView(
    userAccount: UserAccount? = null,
    onEvent: (AccountEvent) -> Unit,
) {
    val authLauncher = rememberLauncherForActivityResult(AuthActivity.AuthResultContract()) {}

    SettingsGroup(title = {
        Text(text = stringResource(R.string.settings_title_account))
    }) {
        if (userAccount != null) {
            SettingsMenuLink(title = {
                Text(text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(id = R.string.auth_placeholder_email))
                        append(": ")
                    }
                    append(userAccount.email)
                })
            }) {}
            if (userAccount.isEmailVerified) {
                SettingsMenuLink(title = {
                    Text(text = stringResource(R.string.email_confirmed))
                }, enabled = false) {}
            } else {
                SettingsMenuLink(title = {
                    Text(text = stringResource(R.string.email_not_confirmed))
                }, subtitle = {
                    Text(text = stringResource(R.string.click_to_send_confirmation_email))
                }) {
                    onEvent(AccountEvent.OnVerifyEmail)
                }
            }
        } else {
            SettingsMenuLink(title = {
                Text(text = stringResource(R.string.auth_signin_to_account))
            }, enabled = true) {
                authLauncher.launch(AuthActivity.AuthAction.SignIn())
            }
        }
    }
}

@Preview
@Composable
private fun AccountSettingsGroupPreview(
    @PreviewParameter(FakeUserAccountProvider::class) userAccount: UserAccount
) {
    AccountSettingsGroupView(userAccount = userAccount) {}
}

@Preview
@Composable
private fun AccountSettingsGroupPreviewNoAccount() {
    AccountSettingsGroupView(userAccount = null) {}
}