package com.rudradave.kmpfintechstarter.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rudradave.kmpfintechstarter.android.ui.FintechApp
import com.rudradave.kmpfintechstarter.android.ui.theme.FintechStarterTheme
import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import org.koin.compose.koinInject

/** Android activity hosting the Compose navigation graph. */
class MainActivity : FragmentActivity() {
    /** Sets up the splash screen and Compose content tree. */
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        setContent {
            val userPrefs = koinInject<UserPreferencesRepository>()
            val isDarkMode by userPrefs.isDarkMode().collectAsState(initial = true)
            FintechStarterTheme(darkTheme = isDarkMode) {
                FintechApp()
            }
        }
    }
}
