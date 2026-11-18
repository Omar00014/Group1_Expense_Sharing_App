package com.example.payment_app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Figma 03 · Sign up / personal details.
// Only draws the UI. Navigation is passed in from ExpenseApp.kt as lambdas
// ([onBack], [onSignUp]); the "= {}" defaults let the preview run without them.
@Composable
fun SignUpScreen(
    onBack: () -> Unit = {},
    onSignUp: () -> Unit = {}
) {
    // Text typed into each field. remember keeps the value across redraws;
    // mutableStateOf redraws the screen when the value changes.
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    // Column stacks everything from top to bottom
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState()) // lets the form scroll on small screens
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoCircle(110.dp)
        Spacer(Modifier.height(24.dp)) // empty gap between elements
        // The input fields come from LoginScreen.kt (same package, no import needed)
        AuthTextField("Full Name", fullName) { fullName = it }
        Spacer(Modifier.height(8.dp))
        AuthTextField("Email Address", email) { email = it }
        Spacer(Modifier.height(8.dp))
        AuthTextField("Password", password) { password = it }
        Spacer(Modifier.height(8.dp))
        AuthTextField("Username", username) { username = it }
        Spacer(Modifier.height(8.dp))

        // Label above the currency box
        Text(
            "Primary currency · USD or EUR",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))

        // Currency box. For now it only looks like a dropdown: it always
        // shows "USD" and does nothing when tapped.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PillLight, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("USD", color = Color.Black, modifier = Modifier.weight(1f)) // weight(1f) pushes the arrow to the right edge
            Text("▼", color = Color.Black)
        }

        Spacer(Modifier.height(16.dp))

        // Back and Sign Up side by side, each taking half of the row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthButton("Back", Modifier.weight(1f), onBack)
            AuthButton("Sign Up", Modifier.weight(1f), onSignUp)
        }
    }
}


// Lets Android Studio show this screen in the Split/Design view
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}