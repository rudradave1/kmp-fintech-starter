package com.rudradave.kmpfintechstarter.android.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechDimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOnboardingFinished: () -> Unit
) {
    val pages = listOf(
        OnboardingPage.Track,
        OnboardingPage.Analyze,
        OnboardingPage.Secure
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            OnboardingBottomBar(
                isLastPage = pagerState.currentPage == pages.size - 1,
                onNextClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onOnboardingFinished()
                    }
                },
                onSkipClick = onOnboardingFinished
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { position ->
                OnboardingContent(page = pages[position])
            }
            
            PagerIndicator(
                count = pages.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
private fun OnboardingContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(FintechDimens.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(200.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OnboardingBottomBar(
    isLastPage: Boolean,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(FintechDimens.screenPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onSkipClick) {
            Text("Skip")
        }
        Button(
            onClick = onNextClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.height(56.dp).width(140.dp)
        ) {
            Text(if (isLastPage) "Get Started" else "Next")
        }
    }
}

@Composable
private fun PagerIndicator(count: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) { iteration ->
            val color = if (currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
            Box(
                modifier = Modifier
                    .size(if (currentPage == iteration) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

sealed class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector
) {
    data object Track : OnboardingPage(
        "Track Spending",
        "Keep an eye on all your transactions in one place, automatically categorized.",
        Icons.Default.AccountBalanceWallet
    )
    data object Analyze : OnboardingPage(
        "Smart Insights",
        "Get deep insights into your spending habits and find ways to save more.",
        Icons.Default.Insights
    )
    data object Secure : OnboardingPage(
        "Enterprise Security",
        "Your data is protected with hardware-backed biometric encryption.",
        Icons.Default.Security
    )
}
