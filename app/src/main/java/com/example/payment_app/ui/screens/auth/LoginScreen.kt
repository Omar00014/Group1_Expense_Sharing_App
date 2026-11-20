
package com.example.payment_app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.payment_app.R

// Colors shared by the authentication screens
val DarkBackground = Color(0xFF010516)
val PillLight = Color(0xFFEFEFF1)

// Additional authentication colors
val AuthBackground = Color(0xFF1A1D23)
val AuthFieldColor = Color(0xFFF0F0F0)
val AuthButtonColor = Color(0xFFD9D9D9)

// Actual FairShare logo
@Composable
fun LogoCircle(size: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.fairshare_logo),
        contentDescription = "FairShare logo",
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(24.dp)),
        contentScale = ContentScale.Fit
    )
}

// Rounded, light-gray text input used on Login and Sign Up.
@Composable
fun AuthTextField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PillLight,
            unfocusedContainerColor = PillLight,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

// Light-gray button. The caller decides what happens on click.
@Composable
fun AuthButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
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

// Login screen with navigation callbacks
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
    onSignUp: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoCircle(150.dp)

        Spacer(Modifier.height(40.dp))

        AuthTextField("Username", username) {
            username = it
        }

        Spacer(Modifier.height(16.dp))

        AuthTextField("Password", password) {
            password = it
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthButton("Sign Up", Modifier.weight(1f), onSignUp)
            AuthButton("Forgot\nPassword?", Modifier.weight(1f), onForgotPassword)
        }

        Spacer(Modifier.height(12.dp))

        AuthButton("Log In", Modifier.fillMaxWidth(), onLogin)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
