package dev.zabolotskikh.authguard.ui.screen.auth.signin

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.zabolotskikh.authguard.R

@Composable
fun ResetPasswordDialog(
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = stringResource(id = R.string.auth_password_recovery_title)) },
        text = { Text(text = stringResource(id = R.string.auth_password_recovery_message)) },
        confirmButton = {
            Button(onClick = { }) { Text(text = stringResource(id = R.string.send)) }
        },
        dismissButton = {
            Button(onClick = { }) { Text(text = stringResource(id = R.string.cancel)) }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ResetPasswordDialogPreview() {
    ResetPasswordDialog()
}