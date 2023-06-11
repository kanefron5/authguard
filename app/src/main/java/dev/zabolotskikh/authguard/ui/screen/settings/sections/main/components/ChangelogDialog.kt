@file:OptIn(ExperimentalTextApi::class)

package dev.zabolotskikh.authguard.ui.screen.settings.sections.main.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.domain.model.ChangelogItem
import dev.zabolotskikh.authguard.domain.model.Release
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsState

@Composable
fun ChangelogDialog(
    onDismiss: () -> Unit = {}, state: SettingsState = SettingsState()
) {

    fun buildChangelogText(
        linkColor: Color
    ) = buildAnnotatedString {
        state.changelog.forEach {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                append(it.version)
                appendLine()
            }
            it.changelog.forEach {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = linkColor,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    pushUrlAnnotation(UrlAnnotation(it.url))
                    append(it.label)
                    pop()
                }
                append(": ")
                append(it.text)
                appendLine()
            }

            appendLine()
            appendLine()
        }
    }

    AlertDialog(modifier = Modifier.fillMaxHeight(.8f),
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.settings_changelog)) },
        text = {
            val text = buildChangelogText(MaterialTheme.colorScheme.primary)
            val uriHandler = LocalUriHandler.current
            ClickableText(modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                onClick = { offset ->
                    text.getUrlAnnotations(offset, offset).firstOrNull()?.let { url ->
                        uriHandler.openUri(url.item.url)
                    }
                })
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        })
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun ChangelogDialogDialogPreview() {
    ChangelogDialog(
        state = SettingsState(
            changelog = listOf(
                Release(
                    "v0.1.0", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem(
                            "TEST-2",
                            "kdkfkdkog kodkgodkgkdk fkdkogkodkgodkgkdkfkdkogkodkgodkgkdkfkdkogkodkgodkgkdkfkdkogkodkgodkg",
                            "https://example.com/"
                        ),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-4", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                ),
                Release(
                    "v0.0.1", setOf(
                        ChangelogItem("TEST-1", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-2", "kdkfkdkogkodkgodkg", "https://example.com/"),
                        ChangelogItem("TEST-3", "kdkfkdkogkodkgodkg", "https://example.com/"),
                    )
                ),
            )
        )
    )
}