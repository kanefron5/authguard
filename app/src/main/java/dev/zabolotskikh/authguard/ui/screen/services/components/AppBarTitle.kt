package dev.zabolotskikh.authguard.ui.screen.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.BuildConfig
import dev.zabolotskikh.authguard.R

@Composable
fun AppBarTitle(
    modifier: Modifier = Modifier,
    isDebug: Boolean = BuildConfig.DEBUG
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.app_name))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isDebug) "debug" else "βeta",
            fontSize = 12.sp,
            color = if (isDebug) colorScheme.onError else colorScheme.onPrimary,
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(if (isDebug) colorScheme.error else colorScheme.primary)
                .padding(10.dp, 4.dp)
        )
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun AppBarTitlePreviewDebug() {
    CenterAlignedTopAppBar(
        title = { AppBarTitle(isDebug = true) }
    )
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun AppBarTitlePreview() {
    CenterAlignedTopAppBar(
        title = { AppBarTitle(isDebug = false) }
    )
}