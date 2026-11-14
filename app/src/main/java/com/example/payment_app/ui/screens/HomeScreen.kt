package com.example.payment_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.payment_app.ui.screens.expense.ChooseGroupSheet
import com.example.payment_app.ui.screens.expense.ExpenseGroup
import com.example.payment_app.ui.screens.expense.ExpenseItem
import com.example.payment_app.ui.screens.expense.ExpenseMockData
import com.example.payment_app.ui.screens.expense.calculateGroupNetBalanceForMember
import com.example.payment_app.ui.screens.expense.calculateNetBalanceForMember
import com.example.payment_app.ui.screens.expense.formatNetBalance
import com.example.payment_app.ui.screens.profile.ScreenTitle
import com.example.payment_app.ui.screens.profile.SettingsCard
import com.example.payment_app.ui.theme.Payment_AppTheme
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    currentUserId: String = ExpenseMockData.john.id,
    groups: List<ExpenseGroup> = ExpenseMockData.sampleGroups,
    expenses: List<ExpenseItem> = ExpenseMockData.initialExpenses,
    onGroupSelectedForAdd: (ExpenseGroup) -> Unit = {},
    onExpenseSelected: (ExpenseItem) -> Unit = {}
) {
    var showChooseGroupSheet by remember { mutableStateOf(false) }

    // Dynamic responsiveness calculations for John Doe
    val netOwedBalance = calculateNetBalanceForMember(currentUserId, expenses)
    val formattedNetBalance = formatNetBalance(netOwedBalance)
    val balanceColor = if (netOwedBalance > 0) Color(0xFF81C784) else if (netOwedBalance < 0) Color(0xFFE57373) else MaterialTheme.colorScheme.onSurface

    val responsiveGroups = groups.map { grp ->
        val grpNet = calculateGroupNetBalanceForMember(currentUserId, grp.id, expenses)
        val grpExpenseCount = expenses.count { it.group.id == grp.id }
        grp.copy(netBalance = grpNet, expenseCount = grpExpenseCount)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: label + avatar for John Doe
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Expenses",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Title
            ScreenTitle("Overview & Expenses")

            // Balance Summary Card (Responsive for John Doe)
            SettingsCard {
                Text(
                    text = if (netOwedBalance >= 0) "Total net owed to you" else "Total net you owe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = formattedNetBalance,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = balanceColor
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${responsiveGroups.size} active groups · ${expenses.size} total expenses",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Recent Expenses Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Expenses",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${expenses.size} items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Expense Item Cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                expenses.forEach { expense ->
                    SettingsCard(onClick = { onExpenseSelected(expense) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = expense.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "${expense.group.name} · Paid by ${expense.payer.fullName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "$${String.format(Locale.US, "%.2f", expense.amount)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button to add expense
        FloatingActionButton(
            onClick = { showChooseGroupSheet = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
                Spacer(Modifier.size(8.dp))
                Text(
                    "Add expense",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Modal Bottom Sheet over HomeScreen
        if (showChooseGroupSheet) {
            ChooseGroupSheet(
                groups = responsiveGroups,
                onGroupSelected = { grp ->
                    showChooseGroupSheet = false
                    onGroupSelectedForAdd(grp)
                },
                onDismiss = { showChooseGroupSheet = false }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun HomeScreenPreview() {
    Payment_AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen()
        }
    }
}
