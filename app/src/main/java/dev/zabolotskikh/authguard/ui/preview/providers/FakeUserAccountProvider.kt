package dev.zabolotskikh.authguard.ui.preview.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.zabolotskikh.authguard.domain.model.UserAccount

class FakeUserAccountProvider : PreviewParameterProvider<UserAccount> {
    override val values: Sequence<UserAccount>
        get() = sequenceOf(
            UserAccount(
                "UserName",
                "usermail@example.com",
                "https://avatars.githubusercontent.com/u/11094644?v=4",
                false,
                "user-id-134959-399595"
            ),
            UserAccount(
                "UserName",
                "usermail@example.com",
                "https://avatars.githubusercontent.com/u/11094644?v=4",
                true,
                "user-id-134959-399595"
            )
        )
}