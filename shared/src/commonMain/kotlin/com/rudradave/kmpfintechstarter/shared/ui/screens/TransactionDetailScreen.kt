package com.rudradave.kmpfintechstarter.shared.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rudradave.kmpfintechstarter.shared.presentation.TransactionDetailViewModel
import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechDimens
import com.rudradave.kmpfintechstarter.shared.util.formattedAmountShared
import org.jetbrains.compose.resources.stringResource
import kmp_fintech_starter.shared.generated.resources.Res
import kmp_fintech_starter.shared.generated.resources.*
import org.koin.core.parameter.parametersOf

data class TransactionDetailScreen(val transactionId: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<TransactionDetailViewModel> { parametersOf(transactionId) }
        val uiState by viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.transaction_detail_title)) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            uiState.transaction?.let { transaction ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(FintechDimens.screenPadding),
                    verticalArrangement = Arrangement.spacedBy(FintechDimens.largeSpacing)
                ) {
                    Text(
                        text = transaction.merchantName,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = transaction.formattedAmountShared(),
                        style = MaterialTheme.typography.displaySmall,
                        color = if (transaction.isDebit) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider()
                    DetailRow(stringResource(Res.string.category_label), transaction.category.name)
                    DetailRow(stringResource(Res.string.status_label), transaction.status.name)
                    DetailRow(stringResource(Res.string.reference_id_label), transaction.id)
                }
            } ?: run {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
