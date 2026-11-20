package com.example.payment_app.ui.screens.balances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Figma 18 · Group balances / owed & owing.
// Shows who owes what inside one group. For now all names and amounts are
// hard-coded sample values and the buttons do nothing yet.
@Composable
fun GroupBalancesScreen() {
    // Column stacks everything from top to bottom, with 12dp between items
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BalancesBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Parts below come from BalancesComponents.kt (same package, no import needed)
        BalancesTopBar("Weekend Trip")
        BalancesHero("You are owed in Weekend Trip", "$62.00") // big headline amount

        // Row puts the two buttons side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // weight(1f) gives each button an equal share of the row
            BalancesPillButton("Remind members", Modifier.weight(1f))
            BalancesPillButton("Record settlement", Modifier.weight(1f))
        }

        BalancesSectionLabel("Group balances", "4 members")

        // One card per member. The last argument (true) underlines the
        // "Remind >" note so it looks like a link.
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BalancesRow("Alex Taylor", "@alex - you", "+$62.00", "owed to you")
            BalancesRow("Jordan @jordan", "Owes Alex", "−$22.00", "Remind >", true)
            BalancesRow("Sam @sam", "Owes Alex", "−$40.00", "Remind >", true)
            BalancesRow("Casey @casey", "No debt", "$0.00", "settled")
        }

        // A spacer with weight(1f) takes all the free space,
        // which pushes the footer text to the bottom of the screen
        Spacer(Modifier.weight(1f))
        Text(
            "4 members - 3 expenses - balances net to $0",
            color = BalancesSubText,
            fontSize = 11.sp
        )
    }
}

// Lets Android Studio show this screen in the Split/Design view
@Preview(showBackground = true)
@Composable
fun GroupBalancesScreenPreview() {
    GroupBalancesScreen()
}