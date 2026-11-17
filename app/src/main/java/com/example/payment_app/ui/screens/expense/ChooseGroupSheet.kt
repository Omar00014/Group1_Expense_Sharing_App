package com.example.payment_app.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.payment_app.ui.theme.Payment_AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseGroupSheet(
    groups: List<ExpenseGroup>,
    onGroupSelected: (ExpenseGroup) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialSelectedGroup: ExpenseGroup? = groups.firstOrNull()
) {
    var selectedGroup by remember { mutableStateOf(initialSelectedGroup ?: groups.firstOrNull()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Sheet Header: Title & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Choose group",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Choose where to add your expense",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Your groups",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Selected",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(10.dp))

            // Group List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                groups.forEach { group ->
                    GroupSelectionCard(
                        group = group,
                        selected = group.id == selectedGroup?.id,
                        onClick = { selectedGroup = group }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Action Button
            PrimaryPillButton(
                text = "Continue to details",
                enabled = selectedGroup != null,
                onClick = {
                    selectedGroup?.let { onGroupSelected(it) }
                }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "${selectedGroup?.name ?: "Group"} selected. You can change this later.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun ChooseGroupSheetPreview() {
    Payment_AppTheme {
        ChooseGroupSheet(
            groups = ExpenseMockData.sampleGroups,
            onGroupSelected = {},
            onDismiss = {}
        )
    }
}
