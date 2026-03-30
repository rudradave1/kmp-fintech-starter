package com.rudradave.kmpfintechstarter.android.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens

@Composable
internal fun ShimmerPlaceholder(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha = transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "alpha",
    )
    Box(
        modifier = modifier
            .alpha(alpha.value)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(FintechDimens.cardCorner),
            ),
    )
}

@Composable
internal fun ScreenLoadingState() {
    Column(
        verticalArrangement = Arrangement.spacedBy(FintechDimens.itemSpacing),
    ) {
        repeat(4) {
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(FintechDimens.shimmerHeight)
                    .padding(horizontal = FintechDimens.screenPadding),
            )
        }
    }
}
