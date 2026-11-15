package com.example.payment_app

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

//Managing Input Values 入力内容を管理
@Composable
fun SignUpScreen() {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    // Overall Screen Layout 画面全体のレイアウト
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoCircle(110.dp)
        Spacer(Modifier.height(24.dp))
        AuthTextField("Full Name", fullName) { fullName = it }
        Spacer(Modifier.height(8.dp))
        AuthTextField("Email Address", email) { email = it }
        Spacer(Modifier.height(8.dp))
        AuthTextField("Password", password) { password = it }
        Spacer(Modifier.height(8.dp))
        AuthTextField("Username", username) { username = it }
        Spacer(Modifier.height(8.dp))

        Text(
            "Primary currency · USD or EUR",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AuthFieldColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("USD", color = Color.Black, modifier = Modifier.weight(1f))
            Text("▼", color = Color.Black)
        }

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthButton("Back", Modifier.weight(1f))
            AuthButton("Sign Up", Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}