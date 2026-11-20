package com.example.payment_app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colors shared by the auth screens (Splash, Login, Sign up)
val DarkBackground = Color(0xFF171A22) // screen background
val PillLight = Color(0xFFEFEFF1) // input fields, buttons and the logo circle

// Round placeholder logo. [size] sets both width and height.
@Composable
fun LogoCircle(size: Dp) {
    Box(
        modifier = Modifier.size(size).background(PillLight, CircleShape),
        contentAlignment = Alignment.Center // keeps the text in the middle of the circle
    ) {
        Text(
            "Placeholder Text\nfor Logo",
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

// Rounded, light-gray text input used on Login and Sign up.
// The screen owns the text: [value] is shown, and [onValueChange] reports every edit.
@Composable
fun AuthTextField(placeholder: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) }, // hint text shown while the field is empty
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PillLight,
            unfocusedContainerColor = PillLight,
            focusedIndicatorColor = Color.Transparent, // hides the underline
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

// Light-gray button. The caller decides what happens on click via [onClick].
@Composable
fun AuthButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PillLight,
            contentColor = Color.Black
        )
    ) {
        Text(text, textAlign = TextAlign.Center)
    }
}

// Figma 02 · Log in.
// The screen only draws the UI. Navigation is passed in from ExpenseApp.kt
// as lambdas (onLogin, onSignUp, onForgotPassword); the "= {}" defaults make
// the preview work without them.
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
    onSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    // Text typed into the fields. remember keeps the value across redraws;
    // mutableStateOf makes the screen redraw when the value changes.
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Column stacks everything from top to bottom
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoCircle(150.dp)
        Spacer(Modifier.height(40.dp)) // empty gap between elements
        AuthTextField("Username", username) { username = it }
        Spacer(Modifier.height(16.dp))
        AuthTextField("Password", password) { password = it }
        Spacer(Modifier.height(16.dp))

        // Row puts the two buttons side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // weight(1f) gives each button an equal share of the row
            AuthButton("Sign Up", Modifier.weight(1f), onSignUp)
            AuthButton("Forgot\nPassword?", Modifier.weight(1f), onForgotPassword)
        }
        Spacer(Modifier.height(12.dp))
        AuthButton("Log In", Modifier.fillMaxWidth(), onLogin)
    }
}

// Lets Android Studio show this screen in the Split/Design view
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}