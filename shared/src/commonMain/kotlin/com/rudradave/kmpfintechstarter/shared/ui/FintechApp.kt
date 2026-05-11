package com.rudradave.kmpfintechstarter.shared.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.rudradave.kmpfintechstarter.shared.domain.repository.UserPreferencesRepository
import com.rudradave.kmpfintechstarter.shared.ui.navigation.RootScreen
import com.rudradave.kmpfintechstarter.shared.ui.theme.FintechStarterTheme
import org.koin.compose.koinInject

/** The main entry point for the shared Compose UI. */
@Composable
fun FintechApp(
    darkTheme: Boolean? = null
) {
    val userPrefs = koinInject<UserPreferencesRepository>()
    
    // Remember the flow instance and collect it.
    // initial = null ensures we don't accidentally fall back to system theme 
    // for a frame while loading from DataStore if a preference already exists.
    val isDarkModePref by remember(userPrefs) { userPrefs.isDarkMode() }
        .collectAsState(initial = null)
    
    LaunchedEffect(isDarkModePref) {
        println("FintechApp: isDarkModePref changed to: $isDarkModePref")
    }
    
    // Final mode: parameter > preference > system.
    val useDarkMode = darkTheme ?: isDarkModePref ?: isSystemInDarkTheme()
    
    val rootScreen = remember { RootScreen() }
    
    FintechStarterTheme(darkTheme = useDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Navigator(rootScreen)
        }
    }
}
