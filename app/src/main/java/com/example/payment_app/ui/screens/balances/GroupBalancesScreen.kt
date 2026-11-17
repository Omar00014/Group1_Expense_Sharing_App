package com.example.payment_app.ui.screens.balances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//Overall Screen Layout 画面全体のレイアウト
@Composable
fun GroupBalancesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BalancesBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BalancesTopBar("Weekend Trip")
        BalancesHero("You are owed in Weekend Trip", "$62.00")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BalancesPillButton("Remind members", Modifier.weight(1f))
            BalancesPillButton("Record settlement", Modifier.weight(1f))
        }

        BalancesSectionLabel("Group balances", "4 members")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BalancesRow("Alex Taylor", "@alex - you", "+$62.00", "owed to you")
            BalancesRow("Jordan @jordan", "Owes Alex", "−$22.00", "Remind >", true)
            BalancesRow("Sam @sam", "Owes Alex", "−$40.00", "Remind >", true)
            BalancesRow("Casey @casey", "No debt", "$0.00", "settled")
        }

        Spacer(Modifier.weight(1f))
        Text(
            "4 members - 3 expenses - balances net to $0",
            color = BalancesSubText,
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupBalancesScreenPreview() {
    GroupBalancesScreen()
}