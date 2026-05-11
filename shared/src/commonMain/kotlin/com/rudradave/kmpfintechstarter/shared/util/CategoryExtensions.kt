package com.rudradave.kmpfintechstarter.shared.util

import androidx.compose.runtime.Composable
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionCategory
import com.rudradave.kmpfintechstarter.shared.domain.model.TransactionStatus
import org.jetbrains.compose.resources.stringResource
import kmp_fintech_starter.shared.generated.resources.Res
import kmp_fintech_starter.shared.generated.resources.*

@Composable
fun TransactionCategory.getLabel(): String = when (this) {
    TransactionCategory.FOOD -> stringResource(Res.string.category_food)
    TransactionCategory.TRANSPORT -> stringResource(Res.string.category_transport)
    TransactionCategory.SHOPPING -> stringResource(Res.string.category_shopping)
    TransactionCategory.ENTERTAINMENT -> stringResource(Res.string.category_entertainment)
    TransactionCategory.UTILITIES -> stringResource(Res.string.category_utilities)
    TransactionCategory.HEALTH -> stringResource(Res.string.category_health)
    TransactionCategory.TRANSFER -> stringResource(Res.string.category_transfer)
    TransactionCategory.OTHER -> stringResource(Res.string.category_other)
}

@Composable
fun TransactionStatus.getLabel(): String = when (this) {
    TransactionStatus.PENDING -> stringResource(Res.string.status_pending)
    TransactionStatus.COMPLETED -> stringResource(Res.string.status_completed)
    TransactionStatus.FAILED -> stringResource(Res.string.status_failed)
    TransactionStatus.REFUNDED -> stringResource(Res.string.status_refunded)
}
