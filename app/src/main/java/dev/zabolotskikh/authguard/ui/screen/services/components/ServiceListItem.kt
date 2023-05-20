@file:OptIn(ExperimentalFoundationApi::class)

package dev.zabolotskikh.authguard.ui.screen.services.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.ui.screen.services.ServiceState

@Composable
fun ServiceListItem(
    modifier: Modifier = Modifier,
    service: Service,
    state: ServiceState,
    onDelete: () -> Unit,
    onChangeFavorite: (Boolean) -> Unit
) {
    fun formatCode(code: String): String? = try {
        if (state.isPrivateMode) "*** ***"
        else code.substring(0..2) + " " + code.substring(3..5)
    } catch (e: Exception) {
        null
    }

    var resetConfirmationDialogShowed by rememberSaveable { mutableStateOf(false) }

    if (resetConfirmationDialogShowed) {
        AlertDialog(onDismissRequest = { resetConfirmationDialogShowed = false },
            title = { Text(text = stringResource(id = R.string.confirm_action)) },
            text = {
                Text(text = stringResource(id = R.string.reset_service_warning))
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        resetConfirmationDialogShowed = false
                    }, colors = ButtonDefaults.buttonColors(
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    text = service.name,
                    color = MaterialTheme.colorScheme.secondary
                )
                IconToggleButton(
                    checked = service.isFavorite,
                    onCheckedChange = onChangeFavorite,
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        checkedContainerColor = MaterialTheme.colorScheme.secondary,
                        checkedContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.StarRate, contentDescription = "Star"
                    )
                }
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ), onClick = { resetConfirmationDialogShowed = true }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete, contentDescription = "Delete"
                    )
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = 42.sp,
                text = formatCode(service.currentCode)
                    ?: stringResource(id = R.string.code_generation_error),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
        if (service.timeoutTime > 0) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = ((service.codeTtl.toFloat() / service.timeoutTime))
            )
        }
    }
}

@Preview(name = "Preview not favorite", widthDp = 400, heightDp = 150)
@Composable
fun ServiceListItemPreview1() {
    ServiceListItem(
        service = Service(name = "name of service", privateKey = "123", currentCode = "123123", codeTtl = 15, timeoutTime = 30),
        state = ServiceState(),
        onDelete = {},
        onChangeFavorite = {}
    )
}

@Preview(name = "Preview favorite", widthDp = 400, heightDp = 150)
@Composable
fun ServiceListItemPreview2() {
    ServiceListItem(
        service = Service(isFavorite = true, name = "name of service", privateKey = "123", currentCode = "123123", codeTtl = 15, timeoutTime = 30),
        state = ServiceState(),
        onDelete = {},
        onChangeFavorite = {}
    )
}

@Preview(name = "Preview long name", widthDp = 400, heightDp = 150)
@Composable
fun ServiceListItemPreview3() {
    ServiceListItem(
        service = Service(name = "servicenameservicenameservicenameservicenameservicenameservicename", privateKey = "123", currentCode = "123123", codeTtl = 15, timeoutTime = 30),
        state = ServiceState(),
        onDelete = {},
        onChangeFavorite = {}
    )
}

@Preview(name = "Preview private mode", widthDp = 400, heightDp = 150)
@Composable
fun ServiceListItemPreview4() {
    ServiceListItem(
        service = Service(name = "name of service", privateKey = "123", currentCode = "123123", codeTtl = 15000, timeoutTime = 30000),
        state = ServiceState(isPrivateMode = true),
        onDelete = {},
        onChangeFavorite = {}
    )
}