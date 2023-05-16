package dev.zabolotskikh.authguard.ui.screen.passkey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasskeyScreen() {
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Enter your password", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordInput(password = password) {
            password = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Handle password submission */ }) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun PasswordInput(password: String, onPasswordChanged: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(4) { index ->
            PasswordDigit(
                digit = password.getOrNull(index)?.toString() ?: "",
                onDigitChanged = { newDigit ->
                    val newPassword = password.toMutableList()
                    if (newDigit.isEmpty()) {
                        newPassword.removeAt(index)
                        if (index > 0) {
                            focusRequester.requestFocus()
                        }
                    } else {
                        newPassword.add(index, newDigit[0])
                        if (index < 3) {
                            focusRequester.requestFocus()
                        }
                    }
                    onPasswordChanged(newPassword.joinToString(""))
                },
                modifier = if (index == 0) {
                    Modifier.focusRequester(focusRequester)
                } else {
                    Modifier
                }.width(64.dp)
            )
        }
    }
}

@Composable
fun PasswordDigit(digit: String, onDigitChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = digit,
        onValueChange = { newValue: String ->
            val filteredValue = newValue.takeLast(1)
            if (digit != filteredValue) {
                onDigitChanged(filteredValue)
            }
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        maxLines = 1,
        singleLine = true,
        modifier = modifier
            .padding(8.dp)
            .height(64.dp)
            .width(64.dp),
        textStyle = TextStyle(fontSize = 24.sp),
//        textAlign = TextAlign.Center
    )
}