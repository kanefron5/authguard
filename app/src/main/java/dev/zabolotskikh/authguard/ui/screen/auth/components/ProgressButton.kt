package dev.zabolotskikh.authguard.ui.screen.auth.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgressButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isProgress: Boolean = false,
) {
    Button(
        enabled = !isProgress && enabled,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    trackColor = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(text = text)
        }
    }
}

@Preview
@Composable
private fun ProgressButtonPreview() {
    ProgressButton(text = "Test", onClick = { /*TODO*/ })
}

@Preview
@Composable
private fun ProgressButtonPreviewProgress() {
    ProgressButton(text = "Test", onClick = { /*TODO*/ }, isProgress = true)
}