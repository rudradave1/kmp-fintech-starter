package com.rudradave.kmpfintechstarter.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rudradave.kmpfintechstarter.shared.ui.FintechApp

/** Android activity hosting the Compose navigation graph. */
class MainActivity : FragmentActivity() {
    /** Sets up the splash screen and Compose content tree. */
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        setContent {
            FintechApp()
        }
    }
}
