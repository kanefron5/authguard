package dev.zabolotskikh.authguard.ui.screen.settings.sections.main.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.zabolotskikh.authguard.R

@Composable
fun ResetConfirmationDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.confirm_action)) },
        text = {
            Text(text = stringResource(id = R.string.reset_warning))
        },
        confirmButton = {
            Button(
                onClick = onConfirm, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        })
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun ResetConfirmationDialogPreview() {
    ResetConfirmationDialog()
}