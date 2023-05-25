@file:OptIn(ExperimentalFoundationApi::class)

package dev.zabolotskikh.passlock.ui.activity.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun RoundedPinBackground(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val radius = if (isPressed.value) 10.dp else size / 2f

    val cornerRadius = animateDpAsState(targetValue = radius, label = "")

    Surface(
        tonalElevation = 10.dp, modifier = modifier.clip(RoundedCornerShape(cornerRadius.value))
    ) {
        Box(modifier = Modifier
            .background(color = backgroundColor)
            .size(size)
            .clip(RoundedCornerShape(cornerRadius.value))
            .combinedClickable(onClick = {
                onClick()
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }, onLongClick = {
                onLongClick()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }, interactionSource = interactionSource, indication = rememberRipple()
            ), contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
@Preview
fun RoundedPinBackgroundPreview() {
    RoundedPinBackground {
        Text(text = "OK")
    }
}