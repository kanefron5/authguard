package dev.zabolotskikh.authguard.ui.screen.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zabolotskikh.authguard.R

@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    onValueChanged: (String) -> Unit = {},
    isValid: Boolean = true,
    type: KeyboardType = KeyboardType.Text
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val transformation = if (type != KeyboardType.Password && type != KeyboardType.NumberPassword) {
        VisualTransformation.None
    } else {
        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    }

    TextField(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChanged,
        singleLine = true,
        visualTransformation = transformation,
        isError = !isValid,
        keyboardOptions = KeyboardOptions(keyboardType = type),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
        trailingIcon = {
            if (type == KeyboardType.Password || type == KeyboardType.NumberPassword) {
                val image = if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) stringResource(R.string.hide_password)
                else stringResource(R.string.show_password)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        }
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

@Preview
@Composable
private fun LabeledTextFieldPasswordPreview() {
    LabeledTextField(label = "Password", value = "password", type = KeyboardType.Password)
}