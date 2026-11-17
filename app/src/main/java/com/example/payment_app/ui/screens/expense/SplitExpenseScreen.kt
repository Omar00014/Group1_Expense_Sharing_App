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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.payment_app.ui.screens.profile.PrimaryPillButton
import com.example.payment_app.ui.screens.profile.SecondaryPillButton
import com.example.payment_app.ui.theme.Payment_AppTheme
import java.util.Locale

@Composable
fun SplitExpenseScreen(
    expenseItem: ExpenseItem,
    onConfirmSplit: (ExpenseItem) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedMembersMap = remember {
        mutableStateMapOf<String, Boolean>().apply {
            expenseItem.splits.forEach { put(it.member.id, it.isSelected) }
        }
    }
    var filterTab by remember { mutableStateOf(0) } // 0 = All members, 1 = Selected members

    val allMembers = expenseItem.splits.map { it.member }
    val selectedCount = selectedMembersMap.values.count { it }
    val perPersonAmount = if (selectedCount > 0) expenseItem.amount / selectedCount else 0.0

    val updatedSplits = expenseItem.splits.map { split ->
        val isSel = selectedMembersMap[split.member.id] ?: false
        split.copy(
            isSelected = isSel,
            amount = if (isSel) perPersonAmount else 0.0
        )
    }

    val finalExpenseItem = expenseItem.copy(
        splitType = "Equal split",
        splits = updatedSplits
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        // Top Bar: Back icon + Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onBack)
                    .padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    "Back",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                "Split expense",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Hero Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = expenseItem.group.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$${String.format(Locale.US, "%.2f", expenseItem.amount)}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${expenseItem.title} · Paid by ${expenseItem.payer.fullName} ${expenseItem.payer.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(20.dp))

        // Subheader & Filter Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Split with",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$selectedCount selected",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(10.dp))

        // Filter Segmented Buttons (All members | Selected members)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Surface(
                onClick = { filterTab = 0 },
                shape = RoundedCornerShape(10.dp),
                color = if (filterTab == 0) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "All members",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (filterTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Surface(
                onClick = { filterTab = 1 },
                shape = RoundedCornerShape(10.dp),
                color = if (filterTab == 1) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Selected members",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (filterTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Member Selection List
        val displayedMembers = if (filterTab == 1) {
            allMembers.filter { selectedMembersMap[it.id] == true }
        } else {
            allMembers
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            displayedMembers.forEach { member ->
                val isSel = selectedMembersMap[member.id] ?: false
                MemberSplitRow(
                    member = member,
                    amount = if (isSel) perPersonAmount else 0.0,
                    selected = isSel,
                    showCheckbox = true,
                    onClick = {
                        selectedMembersMap[member.id] = !isSel
                    }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Bottom Calculation Summary Text
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$selectedCount members x $${String.format(Locale.US, "%.2f", perPersonAmount)} = $${String.format(Locale.US, "%.2f", expenseItem.amount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Equal split · no remaining balance",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(20.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryPillButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.weight(1f)
            )
            PrimaryPillButton(
                text = "Confirm split",
                enabled = selectedCount > 0,
                onClick = { onConfirmSplit(finalExpenseItem) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun SplitExpenseScreenPreview() {
    Payment_AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SplitExpenseScreen(
                expenseItem = ExpenseMockData.sampleDinnerExpense,
                onConfirmSplit = {},
                onBack = {}
            )
        }
    }
}
