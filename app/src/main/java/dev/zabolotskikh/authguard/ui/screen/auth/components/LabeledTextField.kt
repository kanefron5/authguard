package dev.zabolotskikh.authguard.ui.screen.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    onValueChanged: (String) -> Unit = {},
    isValid: Boolean = true,
    type: TextFieldType = TextFieldType.PLAIN_TEXT
) {
    TextField(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChanged,
        isError = !isValid,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}

@Preview
@Composable
private fun LabeledTextFieldPreview() {
    LabeledTextField(label = "Username", value = "kanefron5")
}

@Preview
@Composable
private fun LabeledTextFieldErrorPreview() {
    LabeledTextField(label = "Username", value = "kanefron5", isValid = false)
}