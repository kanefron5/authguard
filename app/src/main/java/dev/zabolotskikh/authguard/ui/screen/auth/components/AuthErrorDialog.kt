package dev.zabolotskikh.authguard.ui.screen.auth.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.zabolotskikh.authguard.R

@Composable
fun AuthErrorDialog(
    modifier: Modifier = Modifier,
    exception: Exception,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.auth_error_occurred)) },
        text = {
            Text(text = exception.localizedMessage ?: stringResource(R.string.auth_unknown_error))
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ResetPasswordDialogPreview() {
    AuthErrorDialog(exception = Exception("Error"))
}