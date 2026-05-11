package com.rudradave.kmpfintechstarter.shared.ui

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.navigator.Navigator
import com.rudradave.kmpfintechstarter.shared.ui.navigation.RootScreen
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    FintechApp()
}

