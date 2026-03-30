package com.rudradave.kmpfintechstarter.android.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme

/** Renders a category filter chip with animated selection colors. */
@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = modifier
            .clip(shape)
            .border(width = 1.dp, color = borderColor.value, shape = shape),
        onClick = onClick,
        shape = shape,
        color = containerColor.value,
        tonalElevation = 0.dp,
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor.value,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
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
