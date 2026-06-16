package com.example.pockit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.pockit.ui.theme.PockitTheme

/**
 * MainActivity
 *
 * Single-Activity architecture using Compose.
 *
 * Navigation strategy:
 * - We use a simple `currentScreen` state variable instead of
 *   NavController for this 2-screen flow. This avoids the NavGraph
 *   overhead for a splash → login transition.
 * - For larger apps, replace this with NavHost + NavController.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the status bar transparent so splash fills edge-to-edge
        enableEdgeToEdge()

        setContent {
            PockitTheme {
                PockitApp()
            }
        }
    }
}

/**
 * PockitApp
 *
 * Root composable managing screen state.
 *
 * [showSplash] controls which screen is visible:
 *  - true  → SplashScreen
 *  - false → LoginScreen
 *
 * When SplashScreen calls onSplashComplete(), showSplash flips to false,
 * triggering recomposition and showing LoginScreen.
 */
@Composable
fun PockitApp() {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(
            onSplashComplete = {
                showSplash = false  // Triggers navigation to LoginScreen
            }
        )
    } else {
        LoginScreen()
    }
}