package com.rudradave.kmpfintechstarter.android.ui.components

import androidx.compose.runtime.Composable

/** Maintains backward compatibility for older call sites while using the new shimmer UI. */
@Composable
internal fun ScreenLoadingState() {
    TransactionsLoadingState()
}
