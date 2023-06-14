package dev.zabolotskikh.authguard.ui.screen.settings.sections.passcode.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.zabolotskikh.authguard.ui.preview.providers.FakeLabelProvider
import dev.zabolotskikh.authguard.ui.theme.success

@Composable
fun SettingsPasscodeCheck(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckChange: (Boolean) -> Unit = {},
    title: String = ""
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp)
            .clickable { onCheckChange(!isChecked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked, onCheckedChange = onCheckChange, colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.success,
                uncheckedColor = MaterialTheme.colorScheme.error
            )
        )
        Text(
            textAlign = TextAlign.Justify,
            text = title,
            color = if (isChecked) MaterialTheme.colorScheme.success else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun SettingsPasscodeCheckPreview1(
    @PreviewParameter(FakeLabelProvider::class) param: Pair<String, Boolean>
) {
    SettingsPasscodeCheck(title = param.first, isChecked = param.second)
}
