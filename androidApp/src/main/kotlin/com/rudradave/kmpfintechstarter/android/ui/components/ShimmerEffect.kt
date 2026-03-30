package com.rudradave.kmpfintechstarter.android.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme

/** Draws a reusable shimmer block used by loading placeholders. */
@Composable
internal fun ShimmerEffect(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(FintechDimens.cardCorner),
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer-translate",
    )
    val brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        ),
        start = Offset(translateAnimation.value, 0f),
        end = Offset(translateAnimation.value + 320f, 320f),
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(brush),
    )
}

/** Shows the dashboard shimmer layout. */
@Composable
internal fun DashboardLoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = FintechDimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(FintechDimens.itemSpacing),
    ) {
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(FintechDimens.bankCardHeight)
                .padding(horizontal = FintechDimens.screenPadding),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FintechDimens.screenPadding),
            horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .weight(1f)
                    .height(92.dp),
            )
            ShimmerEffect(
                modifier = Modifier
                    .weight(1f)
                    .height(92.dp),
            )
        }
        repeat(3) {
            TransactionRowShimmer()
        }
    }
}

/** Shows a list-only shimmer layout for the transactions screen. */
@Composable
internal fun TransactionsLoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = FintechDimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(FintechDimens.itemSpacing),
    ) {
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(horizontal = FintechDimens.screenPadding),
        )
        repeat(3) {
            TransactionRowShimmer()
        }
    }
}

@Composable
private fun TransactionRowShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FintechDimens.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing),
    ) {
        ShimmerEffect(
            modifier = Modifier
                .size(FintechDimens.iconCircle)
                .clip(CircleShape),
            shape = RoundedCornerShape(50),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(0.52f)
                    .height(16.dp),
            )
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(0.28f)
                    .height(12.dp),
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ShimmerEffect(
                modifier = Modifier
                    .size(width = 92.dp, height = 16.dp),
            )
            ShimmerEffect(
                modifier = Modifier
                    .size(width = 68.dp, height = 24.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08101F)
@Composable
private fun ShimmerEffectPreview() {
    FintechStarterTheme {
        DashboardLoadingState()
    }
}
