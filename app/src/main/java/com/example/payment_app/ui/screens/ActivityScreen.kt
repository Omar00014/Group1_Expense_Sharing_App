package com.example.payment_app.ui.screens

import com.example.payment_app.ui.viewmodel.ActivityTypeFilter
import com.example.payment_app.ui.viewmodel.ActivityUiState
import com.example.payment_app.ui.viewmodel.ActivityViewModel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.data.model.ActivityEntry
import com.example.payment_app.data.model.ActivityType
import com.example.payment_app.data.model.GroupSummary
import com.example.payment_app.ui.components.InitialAvatar
import com.example.payment_app.ui.components.balanceColor
import com.example.payment_app.ui.components.countLabel
import com.example.payment_app.ui.components.formatMoney
import com.example.payment_app.ui.theme.Payment_AppTheme

@Composable
fun ActivityScreen(
    onOpenNotifications: () -> Unit,
    onEntryClick: (entryId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ActivityViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ActivityContent(
        uiState = uiState,
        onTypeFilterChange = viewModel::onTypeFilterChange,
        onGroupFilterChange = viewModel::onGroupFilterChange,
        onOpenNotifications = onOpenNotifications,
        onEntryClick = onEntryClick,
        modifier = modifier,
    )
}

@Composable
fun ActivityContent(
    uiState: ActivityUiState,
    onTypeFilterChange: (ActivityTypeFilter) -> Unit,
    onGroupFilterChange: (groupId: String?) -> Unit,
    onOpenNotifications: () -> Unit,
    onEntryClick: (entryId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Transaction history",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                )
                if (uiState.isShowingCachedData) CachedChip()
                IconButton(onClick = onOpenNotifications) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                }
            }
            Text(
                text = "Recent expense and settlement activity across your groups.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActivityTypeFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = uiState.typeFilter == filter,
                        onClick = { onTypeFilterChange(filter) },
                        label = { Text(filter.label) },
                    )
                }
            }
        }

        item { NetOwedCard(netBalanceCents = uiState.netBalanceCents, groupCount = uiState.groups.size) }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Recent entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                GroupFilterDropdown(
                    groups = uiState.groups,
                    selectedGroupName = uiState.selectedGroupName,
                    onGroupFilterChange = onGroupFilterChange,
                )
            }
        }

        if (uiState.entries.isEmpty()) {
            item {
                Text(
                    text = "No entries match these filters.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp),
                )
            }
        }
        items(uiState.entries, key = { it.id }) { entry ->
            HistoryEntryCard(entry = entry, onClick = { onEntryClick(entry.id) })
        }
    }
}

@Composable
private fun CachedChip() {
    AssistChip(
        onClick = {},
        enabled = false,
        label = { Text("Cached") },
        leadingIcon = { Icon(Icons.Filled.CloudOff, contentDescription = null, modifier = Modifier.size(16.dp)) },
    )
}

@Composable
private fun NetOwedCard(netBalanceCents: Long, groupCount: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = if (netBalanceCents >= 0) "Net owed to you" else "Net you owe",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatMoney(netBalanceCents, showPlusSign = true),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = balanceColor(netBalanceCents),
            )
            Text(
                text = "Across ${countLabel(groupCount, "group")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun GroupFilterDropdown(
    groups: List<GroupSummary>,
    selectedGroupName: String,
    onGroupFilterChange: (groupId: String?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text("Group: $selectedGroupName")
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("All groups") },
                onClick = {
                    onGroupFilterChange(null)
                    expanded = false
                },
            )
            groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.name) },
                    onClick = {
                        onGroupFilterChange(group.id)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun HistoryEntryCard(entry: ActivityEntry, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InitialAvatar(name = entry.actorName)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${entry.groupName} · ${entry.detail}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = if (entry.type == ActivityType.EXPENSE) "Expense" else "Settlement",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = formatMoney(entry.amountCents), style = MaterialTheme.typography.titleSmall)
                Text(
                    text = entry.dateLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun previewState(): ActivityUiState {
    val repository = FakeOverviewRepository()
    return ActivityUiState(
        groups = repository.groups(),
        entries = repository.activity(),
        netBalanceCents = repository.groups().sumOf { it.balanceCents },
        isShowingCachedData = true,
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun ActivityContentPreview() {
    Payment_AppTheme(dynamicColor = false) {
        Surface {
            ActivityContent(
                uiState = previewState(),
                onTypeFilterChange = {}, onGroupFilterChange = {}, onOpenNotifications = {}, onEntryClick = {},
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun TransactionHistoryEmptyPreview() {
    Payment_AppTheme(darkTheme = true, dynamicColor = false) {
        Surface {
            ActivityContent(
                uiState = previewState().copy(typeFilter = ActivityTypeFilter.SETTLEMENTS, entries = emptyList()),
                onTypeFilterChange = {}, onGroupFilterChange = {}, onOpenNotifications = {}, onEntryClick = {},
            )
        }
    }
}
