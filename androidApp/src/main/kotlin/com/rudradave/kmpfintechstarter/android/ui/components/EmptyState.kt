package com.rudradave.kmpfintechstarter.android.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.android.R
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme

/** Displays a centered empty or error state with an optional action button. */
@Composable
internal fun EmptyState(
    @StringRes titleRes: Int,
    @StringRes bodyRes: Int,
    modifier: Modifier = Modifier,
    @StringRes actionLabelRes: Int = R.string.action_retry,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(FintechDimens.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        EmptyIllustration()
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(bodyRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (onAction != null) {
            Button(onClick = onAction) {
                Text(text = stringResource(actionLabelRes))
            }
        }
    }
}

@Composable
private fun EmptyIllustration() {
    val accent = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val muted = MaterialTheme.colorScheme.outlineVariant
    Canvas(
        modifier = Modifier
            .padding(bottom = FintechDimens.largeSpacing)
            .size(128.dp),
    ) {
        drawCircle(
            color = accent.copy(alpha = 0.12f),
            radius = size.minDimension * 0.48f,
        )
        drawCircle(
            color = accent,
            radius = size.minDimension * 0.22f,
            style = Stroke(width = 8f),
        )
        drawLine(
            color = secondary,
            start = Offset(size.width * 0.32f, size.height * 0.68f),
            end = Offset(size.width * 0.68f, size.height * 0.32f),
            strokeWidth = 10f,
        )
        drawCircle(
            color = muted,
            radius = size.minDimension * 0.05f,
            center = Offset(size.width * 0.25f, size.height * 0.26f),
        )
        drawCircle(
            color = muted,
            radius = size.minDimension * 0.05f,
            center = Offset(size.width * 0.76f, size.height * 0.74f),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
@Composable
private fun EmptyStatePreview() {
    FintechStarterTheme {
        EmptyState(
            titleRes = R.string.empty_transactions_title,
            bodyRes = R.string.empty_transactions_body,
            actionLabelRes = R.string.action_clear_filter,
            onAction = {},
        )
    }
}
