@file:OptIn(ExperimentalFoundationApi::class)

package dev.zabolotskikh.authguard.ui.screen.services.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.domain.model.Service
import dev.zabolotskikh.authguard.ui.screen.services.ServiceState

@Composable
fun ServiceListItem(
    modifier: Modifier = Modifier,
    service: Service,
    state: ServiceState
) {
    fun formatCode(code: String) = try {
        if (state.isPrivateMode) "*** ***"
        else code.substring(0..2) + " " + code.substring(3..5)
    } catch (e: Exception) {
        "ERROR"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(),
                    text = service.name,
                    color = MaterialTheme.colorScheme.secondary
                )
                IconToggleButton(
                    checked = false,
                    onCheckedChange = {},
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.StarRate, contentDescription = "Star"
                    )
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = 42.sp,
                text = formatCode(service.currentCode),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
        if (service.timeoutTime > 0) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = ((service.codeTtl.toFloat() / service.timeoutTime))
            )
        }
    }
}