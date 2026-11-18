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

// Figma 19 · Member balance / debt details.
// Shows how much one member owes you and the shared activity behind it.
// For now all names and amounts are hard-coded sample values.
@Composable
fun MemberBalanceScreen() {
    // true while the "Send reminder" popup (Figma 20) is open.
    // remember keeps the value across redraws; mutableStateOf redraws the
    // screen when the value changes.
    var showReminder by remember { mutableStateOf(false) }

    // Column stacks everything from top to bottom, with 12dp between items
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BalancesBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Parts below come from BalancesComponents.kt (same package, no import needed)
        BalancesTopBar("Jordan @jordan owes you")
        BalancesHero("Weekend Trip · current group-net debt", "$22.00") // big headline amount

        // The trailing { ... } is the button's onClick: it opens the popup
        BalancesPillButton("Remind Jordan", Modifier.fillMaxWidth()) { showReminder = true }

        BalancesSectionLabel("Shared activity")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BalancesRow("Jordan @jordan", "Dinner - $22 per person", "$88.00", "total")
            BalancesRow("Sam", "Settled with Alex", "$22.00", "settled")
        }

        // A spacer with weight(1f) takes all the free space,
        // which pushes the summary below to the bottom of the screen
        Spacer(Modifier.weight(1f))

        // Thin divider line: a 1dp-high box filled with the card color
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BalancesCardColor))
        Row(modifier = Modifier.fillMaxWidth()) {
            // weight(1f) pushes the amount to the right edge
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

    // The popup lives outside the Column. It is drawn only while showReminder
    // is true, and onDismiss sets it back to false to close it.
    if (showReminder) {
        SendReminderSheet(onDismiss = { showReminder = false })
    }
}

// Lets Android Studio show this screen in the Split/Design view
@Preview(showBackground = true)
@Composable
fun MemberBalanceScreenPreview() {
    MemberBalanceScreen()
}