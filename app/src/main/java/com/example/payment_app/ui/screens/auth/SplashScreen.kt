
package com.example.payment_app.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.payment_app.R
import kotlinx.coroutines.delay

private val SplashBackground = Color(0xFF010516)

@Composable
fun SplashScreen(onFinished: () -> Unit = {}) {

    // Display the splash screen for 1.5 seconds
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fairshare_logo),
            contentDescription = "FairShare logo",
            modifier = Modifier.size(280.dp),
            contentScale = ContentScale.Fit
        )
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
