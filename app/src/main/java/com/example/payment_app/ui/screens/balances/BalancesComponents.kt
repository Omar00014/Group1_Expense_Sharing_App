package com.example.payment_app.ui.screens.balances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 仮の色。Figmaの色コードが決まったらここだけ直す
// replace with the actual Figma colors L22~25
val BalancesBackground = Color(0xFF14171C)
val BalancesCardColor = Color(0xFF1E2229)
val BalancesSubText = Color(0xFF8B9099)
val BalancesWhite = Color(0xFFF2F2F2)

// Creating a profile image frame プロフィール画像の枠作成
@Composable
fun BalancesAvatar(size: Dp = 40.dp) {
    Box(modifier = Modifier.size(size).background(Color(0xFFD9D9D9), CircleShape))
}

// Creating buttons ボタン作成
@Composable
fun BalancesPillButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BalancesWhite,
            contentColor = Color.Black
        )
    ) {
        Text(text, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}

// Creating top bar 画面上部のバー作成
@Composable
fun BalancesTopBar(title: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = BalancesSubText, fontSize = 13.sp, modifier = Modifier.weight(1f))
        BalancesAvatar(32.dp)
    }
}

// Displaying the balances 残高を大きく表示する部分の作成
@Composable
fun BalancesHero(label: String, amount: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = BalancesSubText, fontSize = 13.sp)
        Text(amount, color = Color.White, fontSize = 44.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BalancesSectionLabel(left: String, right: String = "") {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            left,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (right.isNotEmpty()) {
            Text(right, color = BalancesSubText, fontSize = 12.sp)
        }
    }
}

// Displaying friend and transaction information 友達や取引の情報を表示させるコード
@Composable
fun BalancesRow(
    title: String,
    subtitle: String,
    amount: String,
    note: String,
    noteUnderlined: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BalancesCardColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BalancesAvatar()
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(subtitle, color = BalancesSubText, fontSize = 11.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(amount, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(
                note,
                color = BalancesSubText,
                fontSize = 11.sp,
                textDecoration = if (noteUnderlined) TextDecoration.Underline else TextDecoration.None
            )
        }
    }
}