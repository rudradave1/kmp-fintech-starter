package com.rudradave.kmpfintechstarter.shared.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechStarterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/** Renders a category filter chip with animated selection colors. */
@Composable
internal fun CategoryChip(
    label: String,
    selected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(999.dp)
    val containerColor = animateColorAsState(
        targetValue = if (selected) selectedColor else MaterialTheme.colorScheme.surface,
        animationSpec = spring(),
        label = "chip-container",
    )
    val contentColor = animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(),
        label = "chip-content",
    )
    val borderColor = animateColorAsState(
        targetValue = if (selected) selectedColor else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = spring(),
        label = "chip-border",
    )

    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        color = containerColor.value,
        border = androidx.compose.foundation.BorderStroke(width = 1.dp, color = borderColor.value),
        tonalElevation = 0.dp,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            style = MaterialTheme.typography.labelLarge,
            color = contentColor.value,
        )
    }
}

@Preview
@Composable
private fun CategoryChipPreview() {
    FintechStarterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CategoryChip(
                label = "Food",
                selected = true,
                selectedColor = MaterialTheme.colorScheme.primary,
                onClick = {},
            )
        }
    }
}
