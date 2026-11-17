package com.example.payment_app.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.payment_app.ui.screens.profile.PrimaryPillButton
import com.example.payment_app.ui.screens.profile.SecondaryPillButton
import com.example.payment_app.ui.screens.profile.SettingsCard
import com.example.payment_app.ui.theme.Payment_AppTheme
import java.util.Locale

@Composable
fun AddExpenseScreen(
    initialGroup: ExpenseGroup,
    onContinueToSplit: (ExpenseItem) -> Unit,
    onSaveDraft: (ExpenseItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    groups: List<ExpenseGroup> = ExpenseMockData.sampleGroups,
    members: List<ExpenseMember> = ExpenseMockData.allMembers
) {
    var title by rememberSaveable { mutableStateOf("Dinner") }
    var amountInput by rememberSaveable { mutableStateOf("88.00") }
    var selectedGroup by remember { mutableStateOf(initialGroup) }
    var selectedPayer by remember { mutableStateOf(members.firstOrNull { it.username == "@jordan" } ?: members.first()) }
    var dateString by rememberSaveable { mutableStateOf("Today, 12:30 PM") }
    var receiptName by rememberSaveable { mutableStateOf<String?>(null) }

    val amountDouble = amountInput.toDoubleOrNull() ?: 0.0
    val memberCount = selectedGroup.memberCount
    val perPersonAmount = if (memberCount > 0) amountDouble / memberCount else 0.0

    val currentExpenseItem = ExpenseItem(
        id = "new_expense",
        title = title.ifBlank { "Untitled Expense" },
        amount = amountDouble,
        currency = "USD",
        payer = selectedPayer,
        group = selectedGroup,
        date = dateString,
        splitType = "Equal split",
        splits = members.take(memberCount).map {
            ExpenseSplit(it, perPersonAmount, true)
        },
        receiptName = receiptName
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "New expense",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Add expense",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
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
                    placeholder = { Text("What was this for?") },
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

            // Payer Selection Row
            FormSelectionRow(
                label = "Payer",
                value = "${selectedPayer.fullName} ${selectedPayer.username}",
                onClick = {
                    // Toggle through sample members for demo
                    val nextIdx = (members.indexOf(selectedPayer) + 1) % members.size
                    selectedPayer = members[nextIdx]
                }
            )

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
                    prefix = { Text("$ ") },
                    suffix = { Text("USD") },
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

            // Group Selection Row
            FormSelectionRow(
                label = "Group",
                value = "${selectedGroup.name} (${selectedGroup.memberCount} members)",
                onClick = {
                    val nextIdx = (groups.indexOf(selectedGroup) + 1) % groups.size
                    selectedGroup = groups[nextIdx]
                }
            )

            // Date Selection Row
            SettingsCard(onClick = { /* Date picker mock */ }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Date",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            dateString,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Split Summary Preview Card
            SettingsCard(onClick = { onContinueToSplit(currentExpenseItem) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Split summary",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Equal split or custom split",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "$memberCount people x $${String.format(Locale.US, "%.2f", perPersonAmount)} = $${String.format(Locale.US, "%.2f", amountDouble)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Receipt Dropzone Card
            SettingsCard(onClick = {
                receiptName = if (receiptName == null) "Receipt_Photo.jpg" else null
            }) {
                Text(
                    "Receipt (optional)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    receiptName ?: "Add receipt photo or file",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (receiptName != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Action Buttons
        PrimaryPillButton(
            text = "Continue",
            onClick = { onContinueToSplit(currentExpenseItem) },
            enabled = title.isNotBlank() && amountDouble > 0
        )

        Spacer(Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SecondaryPillButton(
                text = "Save draft",
                onClick = { onSaveDraft(currentExpenseItem) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun AddExpenseScreenPreview() {
    Payment_AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AddExpenseScreen(
                initialGroup = ExpenseMockData.weekendTripGroup,
                onContinueToSplit = {},
                onSaveDraft = {},
                onDismiss = {}
            )
        }
    }
}
