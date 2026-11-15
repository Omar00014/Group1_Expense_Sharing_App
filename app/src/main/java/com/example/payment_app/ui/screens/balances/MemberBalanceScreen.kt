package com.example.payment_app.ui.screens.balances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MemberBalanceScreen() {
    var showReminder by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BalancesBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BalancesTopBar("Jordan @jordan owes you")
        BalancesHero("Weekend Trip · current group-net debt", "$22.00")
        BalancesPillButton("Remind Jordan", Modifier.fillMaxWidth()) { showReminder = true }

        BalancesSectionLabel("Shared activity")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BalancesRow("Jordan @jordan", "Dinner - $22 per person", "$88.00", "total")
            BalancesRow("Sam", "Settled with Alex", "$22.00", "settled")
        }

        Spacer(Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BalancesCardColor))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Current balance", color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
            Text("$22.00", color = Color.White, fontSize = 13.sp)
        }
        Text("Jordan @jordan owes Alex @alex", color = BalancesSubText, fontSize = 11.sp)
        Text(
            "Balance includes all shared activity,\nnot just the Dinner expense.",
            color = BalancesSubText,
            fontSize = 11.sp
        )
    }

    if (showReminder) {
        SendReminderSheet(onDismiss = { showReminder = false })
    }
}

@Preview(showBackground = true)
@Composable
fun MemberBalanceScreenPreview() {
    MemberBalanceScreen()
}