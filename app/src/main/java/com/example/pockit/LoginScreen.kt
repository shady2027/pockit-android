package com.example.pockit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pockit.ui.theme.BrandGreen
import com.example.pockit.ui.theme.DarkBackground
import com.example.pockit.ui.theme.TextPrimary


/**
 * LoginScreen
 *
 * Placeholder destination screen after the splash.
 * Replace this with your actual login UI.
 */
@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to Pockit",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Login screen goes here",
                fontSize = 14.sp,
                color = BrandGreen
            )
        }
    }
}