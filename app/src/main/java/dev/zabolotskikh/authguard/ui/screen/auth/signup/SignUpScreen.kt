package dev.zabolotskikh.authguard.ui.screen.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.ui.screen.auth.components.LabeledTextField

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(PaddingValues(horizontal = 16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Create an account so you can synchronize 2FA codes between devices",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            LabeledTextField(modifier = Modifier.fillMaxWidth(), label = "Email")
            Spacer(modifier = Modifier.height(8.dp))
            LabeledTextField(modifier = Modifier.fillMaxWidth(), label = "Password")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your password must be 8 or more characters long and contains a mix of upper and lower case letters, numbers and symbols",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
                fontSize = 10.sp,
                textAlign = TextAlign.Justify
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Create an account")
        }

        val signUpText = buildAnnotatedString {
            append("By signing up, you're agree to our")
            append(" ")
            val style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
            withStyle(style) {
                append("Terms of Use")
                this.pushStringAnnotation("terms_of_use", "Terms of Use")
                pop()
            }
            append(" ")
            append("and")
            append(" ")
            withStyle(style) {
                append("Privacy Policy")
                this.pushStringAnnotation("privacy_policy", "Privacy Policy")
                pop()
            }
        }
        ClickableText(
            modifier = Modifier.padding(24.dp).fillMaxWidth(.7f),
            text = signUpText,
            onClick = { offset ->
                signUpText.getStringAnnotations("terms_of_use", offset, offset).firstOrNull()?.let {
                    // TODO
                }
                signUpText.getStringAnnotations("privacy_policy", offset, offset).firstOrNull()?.let {
                    // TODO
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
        )
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen()
}