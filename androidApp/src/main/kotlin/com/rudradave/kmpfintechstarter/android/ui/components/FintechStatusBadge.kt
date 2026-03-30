package com.rudradave.kmpfintechstarter.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme

/** Displays a compact pill badge for a transaction status. */
@Composable
internal fun FintechStatusBadge(
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(
            color = tint.copy(alpha = 0.18f),
            shape = RoundedCornerShape(100.dp),
        ),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelLarge,
            color = tint,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
@Composable
private fun FintechStatusBadgePreview() {
    FintechStarterTheme {
        FintechStatusBadge(
            label = "Completed",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
