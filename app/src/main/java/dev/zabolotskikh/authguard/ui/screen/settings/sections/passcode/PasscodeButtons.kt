package dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.components.PasscodeKeyboard

@Composable
fun PasscodeButtons(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    onSubmit: (String) -> Unit = {},
    onEdit: (String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize().padding(vertical = 48.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
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
    PasscodeButtons(
        title = {
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
        }
    )
}