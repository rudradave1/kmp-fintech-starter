package com.rudradave.kmpfintechstarter.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rudradave.kmpfintechstarter.android.ui.FintechApp
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme

/** Android activity hosting the Compose navigation graph. */
class MainActivity : ComponentActivity() {
    /** Sets up the splash screen and Compose content tree. */
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            FintechStarterTheme {
                FintechApp()
            }
        }
    }
}
