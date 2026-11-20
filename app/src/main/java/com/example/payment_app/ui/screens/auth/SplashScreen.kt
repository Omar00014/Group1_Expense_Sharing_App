package com.example.payment_app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Figma 01 · Splash.
// Shows the logo for a moment, then calls [onFinished] so ExpenseApp.kt can
// move on to the Login screen. The "= {}" default lets the preview run without it.
@Composable
fun SplashScreen(onFinished: () -> Unit = {}) {
    // LaunchedEffect runs its block once when the screen first appears.
    // Unit as the key means "don't run it again when the screen redraws".
    LaunchedEffect(Unit) {
        delay(1500) // wait 1.5 seconds without freezing the UI
        onFinished() // tell the app that the splash is done
    }

    // Box places its content on top of each other; here it just centers the logo
    Box(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        // LogoCircle and DarkBackground come from LoginScreen.kt (same package, no import needed)
        LogoCircle(180.dp)
    }
}

// Lets Android Studio show this screen in the Split/Design view
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}