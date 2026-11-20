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

// Shared building blocks for the Balances screens (Figma 05: screens 18-20).
// Colors are placeholders estimated from the Figma; update them here once the
// final color codes are agreed.
val BalancesBackground = Color(0xFF14171C) // screen background
val BalancesCardColor = Color(0xFF1E2229) // rows and the message box
val BalancesSubText = Color(0xFF8B9099) // gray secondary text
val BalancesWhite = Color(0xFFF2F2F2) // pill buttons

// Gray circle standing in for a profile picture. [size] sets width and height.
@Composable
fun BalancesAvatar(size: Dp = 40.dp) {
    Box(modifier = Modifier.size(size).background(Color(0xFFD9D9D9), CircleShape))
}

// White, fully rounded button (e.g. "Remind members", "Send").
// The caller decides what happens on click via [onClick].
@Composable
fun BalancesPillButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(22.dp), // half of the height, so the ends are fully round
        colors = ButtonDefaults.buttonColors(
            containerColor = BalancesWhite,
            contentColor = Color.Black
        )
    ) {
        Text(text, fontWeight = FontWeight.Medium, fontSize = 13.sp)
    }
}

// Top row of a screen: a title on the left and an avatar on the right.
@Composable
fun BalancesTopBar(title: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        // weight(1f) lets the title take all the free space, which pushes the avatar to the right edge
        Text(title, color = BalancesSubText, fontSize = 13.sp, modifier = Modifier.weight(1f))
        BalancesAvatar(32.dp)
    }
}

// Centered headline: a small [label] above a big [amount] (e.g. "$62.00").
@Composable
fun BalancesHero(label: String, amount: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = BalancesSubText, fontSize = 13.sp)
        Text(amount, color = Color.White, fontSize = 44.sp, fontWeight = FontWeight.Bold)
    }
}

// Section heading with an optional gray text on the right (e.g. "Group balances" / "4 members").
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
        // only draw the right-hand text when one was given
        if (right.isNotEmpty()) {
            Text(right, color = BalancesSubText, fontSize = 12.sp)
        }
    }
}

// One card in a list: avatar, name and subtitle on the left,
// amount and a small note on the right. [noteUnderlined] makes the note look like a link ("Remind >").
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
            .background(BalancesCardColor, RoundedCornerShape(12.dp)) // background goes before padding, so the padding sits inside the card
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BalancesAvatar()
        Spacer(Modifier.width(12.dp)) // empty gap between avatar and text

        // Left column: name and subtitle (takes the free space)
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(subtitle, color = BalancesSubText, fontSize = 11.sp)
        }

        // Right column: amount and note, aligned to the right edge
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