package com.example.payment_app.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.payment_app.ui.screens.profile.PrimaryPillButton
import com.example.payment_app.ui.screens.profile.SecondaryPillButton
import com.example.payment_app.ui.theme.Payment_AppTheme
import java.util.Locale

@Composable
fun EditExpenseScreen(
    expenseItem: ExpenseItem,
    onSave: (ExpenseItem) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    groups: List<ExpenseGroup> = ExpenseMockData.sampleGroups,
    members: List<ExpenseMember> = ExpenseMockData.allMembers
) {
    var title by rememberSaveable { mutableStateOf(expenseItem.title) }
    var amountInput by rememberSaveable { mutableStateOf(expenseItem.amount.toString()) }
    var selectedPayer by remember { mutableStateOf(expenseItem.payer) }
    var selectedGroup by remember { mutableStateOf(expenseItem.group) }
    var dateString by rememberSaveable { mutableStateOf(expenseItem.date) }
    var receiptName by rememberSaveable { mutableStateOf(expenseItem.receiptName) }

    val amountDouble = amountInput.toDoubleOrNull() ?: 0.0
    val activeMembers = expenseItem.splits.filter { it.isSelected }.map { it.member }
    val memberCount = if (activeMembers.isNotEmpty()) activeMembers.size else selectedGroup.memberCount
    val perPersonAmount = if (memberCount > 0) amountDouble / memberCount else 0.0
    val memberNamesList = activeMembers.joinToString(", ") { it.fullName.split(" ").first() }

    val updatedExpense = expenseItem.copy(
        title = title.ifBlank { "Untitled Expense" },
        amount = amountDouble,
        payer = selectedPayer,
        group = selectedGroup,
        date = dateString,
        receiptName = receiptName
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Edit expense",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PFP",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF171A22),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Form Fields
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Title Input
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Title",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Amount Input
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Amount",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    prefix = { Text("USD ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Paid by Selector Row
            FormSelectionRow(
                label = "Paid by",
                value = "${selectedPayer.fullName} ${selectedPayer.username}",
                onClick = {
                    val nextIdx = (members.indexOf(selectedPayer) + 1) % members.size
                    selectedPayer = members[nextIdx]
                }
            )

            // Group Selector Row
            FormSelectionRow(
                label = "Group",
                value = selectedGroup.name,
                onClick = {
                    val nextIdx = (groups.indexOf(selectedGroup) + 1) % groups.size
                    selectedGroup = groups[nextIdx]
                }
            )

            // Date Selector Row
            FormSelectionRow(
                label = "Date",
                value = dateString,
                onClick = {
                    dateString = if (dateString == "Today") "Yesterday" else "Today"
                }
            )

            // Split Selector Row
            FormSelectionRow(
                label = "Split",
                value = "Equal - $memberCount x $${String.format(Locale.US, "%.2f", perPersonAmount)}",
                subtitle = if (memberNamesList.isNotBlank()) memberNamesList else null,
                onClick = { /* Split config trigger */ }
            )

            // Receipt Selector Row
            FormSelectionRow(
                label = "Receipt (optional)",
                value = receiptName ?: "Add or replace receipt",
                onClick = {
                    receiptName = if (receiptName == null) "Updated_Receipt.jpg" else null
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        // Status Subtitle Note
        Text(
            text = "Changes save to ${selectedGroup.name}\nSync status appears here after saving",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // Action Buttons
        PrimaryPillButton(
            text = "Save",
            enabled = title.isNotBlank() && amountDouble > 0,
            onClick = { onSave(updatedExpense) }
        )

        Spacer(Modifier.height(10.dp))

        SecondaryPillButton(
            text = "Cancel",
            onClick = onCancel
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun EditExpenseScreenPreview() {
    Payment_AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EditExpenseScreen(
                expenseItem = ExpenseMockData.sampleDinnerExpense,
                onSave = {},
                onCancel = {}
            )
        }
    }
}
