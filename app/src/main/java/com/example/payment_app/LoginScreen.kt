package com.example.payment_app

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

// 仮の色。Figmaの色コードが決まったらここだけ直す
// TODO: replace with the actual Figma colors L20~22
val AuthBackground = Color(0xFF1A1D23)
val AuthFieldColor = Color(0xFFF0F0F0)
val AuthButtonColor = Color(0xFFD9D9D9)

//Logo Circle 丸いロゴ
@Composable
fun LogoCircle(size: Dp) {
    Box(
        modifier = Modifier.size(size).background(AuthButtonColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Placeholder Text\nfor Logo",
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

//Auth Text Field 入力欄
@Composable
fun AuthTextField(placeholder: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AuthFieldColor,
            unfocusedContainerColor = AuthFieldColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

//Auth Button ボタン作成
@Composable
fun AuthButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthButtonColor,
            contentColor = Color.Black
        )
    ) {
        Text(text, textAlign = TextAlign.Center)
    }
}

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoCircle(150.dp)
        Spacer(Modifier.height(40.dp))
        AuthTextField("Username", username) { username = it }
        Spacer(Modifier.height(16.dp))
        AuthTextField("Password", password) { password = it }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthButton("Sign Up", Modifier.weight(1f))
            AuthButton("Forgot\nPassword?", Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        AuthButton("Log In", Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}