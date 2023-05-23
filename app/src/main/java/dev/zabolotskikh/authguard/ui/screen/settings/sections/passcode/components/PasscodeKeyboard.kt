package dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.SubdirectoryArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasscodeKeyboard(
    modifier: Modifier = Modifier, space: Dp = 20.dp, onKey: (Char) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        @Composable
        fun ButtonGroup(vararg buttons: Char) = Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for ((index, s) in buttons.withIndex()) {
                RoundedPinBackground(onClick = { onKey(s) }) {
                    Text(
                        text = s.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (index != buttons.lastIndex) {
                    Spacer(modifier = Modifier.width(space))
                }
            }
        }

        ButtonGroup('1', '2', '3')
        Spacer(modifier = Modifier.height(space))
        ButtonGroup('4', '5', '6')
        Spacer(modifier = Modifier.height(space))
        ButtonGroup('7', '8', '9')
        Spacer(modifier = Modifier.height(space))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedPinBackground(
                onClick = { onKey('\b') },
                onLongClick = { onKey('\r') },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Outlined.Backspace,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(space))
            RoundedPinBackground(onClick = { onKey('0') }) {
                Text(
                    text = "0",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.width(space))
            RoundedPinBackground(
                onClick = { onKey('\n') },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Outlined.SubdirectoryArrowLeft,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
@Preview
private fun PasscodeKeyboardPreview() {
    PasscodeKeyboard()
}