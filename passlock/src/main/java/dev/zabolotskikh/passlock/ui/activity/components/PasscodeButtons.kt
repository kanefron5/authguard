package dev.zabolotskikh.passlock.ui.activity.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.passlock.R

@Composable
internal fun PasscodeButtons(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    onSubmit: (String) -> Unit = {},
    onEdit: (String) -> Unit = {},
    isCancelButton: Boolean = false,
    onCancel: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 48.dp, top = if (isCancelButton) 0.dp else 48.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isCancelButton) {
                TextButton(modifier = Modifier.padding(16.dp), onClick = onCancel) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            title?.invoke()
        }
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var currentPasswd by rememberSaveable { mutableStateOf("") }

            TextField(
                value = currentPasswd,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { },
                enabled = false,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle.Default.copy(fontSize = 48.sp, textAlign = TextAlign.Center)
            )


            PasscodeKeyboard(modifier = Modifier.fillMaxWidth(), onKey = {
                when (it) {
                    '\b' -> currentPasswd = currentPasswd.dropLast(1)
                    '\r' -> currentPasswd = ""
                    '\n' -> {
                        onSubmit(currentPasswd)
                        currentPasswd = ""
                    }

                    else -> currentPasswd += it
                }
                onEdit(currentPasswd)
            })

        }
    }
}

@Preview(showSystemUi = true, device = PIXEL_4)
@Composable
private fun PasscodeButtonsPreview1() {
    PasscodeButtons()
}

@Preview(showSystemUi = true, device = PIXEL_4)
@Composable
private fun PasscodeButtonsPreview2() {
    PasscodeButtons(isCancelButton = true)
}

@Preview(showSystemUi = true, device = PIXEL_4)
@Composable
private fun PasscodeButtonsPreview3() {
    PasscodeButtons(title = {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Введите код-пароль",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Осталось попыток n",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    })
}

@Preview(showSystemUi = true, device = PIXEL)
@Composable
private fun PasscodeButtonsPreview4() {
    PasscodeButtons(isCancelButton = true, title = {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Введите код-пароль",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Осталось попыток n",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    })
}