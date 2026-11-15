package com.example.payment_app.ui.screens

import com.example.payment_app.ui.viewmodel.HomeUiState
import com.example.payment_app.ui.viewmodel.HomeViewModel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.payment_app.ui.components.GroupSummaryCard
import com.example.payment_app.ui.components.InitialAvatar
import com.example.payment_app.ui.components.SectionHeader
import com.example.payment_app.ui.components.formatMoney
import com.example.payment_app.ui.components.netBalanceCents
import com.example.payment_app.ui.screens.groups.GroupSummary
import com.example.payment_app.ui.screens.groups.sampleGroups
import com.example.payment_app.ui.theme.Payment_AppTheme
import kotlin.math.abs

@Composable
fun HomeScreen(
    groups: List<GroupSummary>,
    onAddExpense: () -> Unit,
    onPayShare: () -> Unit,
    onSeeAllGroups: () -> Unit,
    onGroupClick: (groupId: String) -> Unit,
    onSeeAllActivity: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        groups = groups,
        onAddExpense = onAddExpense,
        onPayShare = onPayShare,
        onSeeAllGroups = onSeeAllGroups,
        onGroupClick = onGroupClick,
        onSeeAllActivity = onSeeAllActivity,
        onProfileClick = onProfileClick,
        modifier = modifier,
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    groups: List<GroupSummary>,
    onAddExpense: () -> Unit,
    onPayShare: () -> Unit,
    onSeeAllGroups: () -> Unit,
    onGroupClick: (groupId: String) -> Unit,
    onSeeAllActivity: () -> Unit,
    onProfileClick: () -> Unit,
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
                    text = "${uiState.greeting}, ${uiState.userFirstName}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                InitialAvatar(
                    name = uiState.userFirstName,
                    modifier = Modifier.clickable(onClickLabel = "Open profile", onClick = onProfileClick),
                )
            }
        }

        item { NetBalanceHeader(netBalanceCents = netBalanceCents(groups)) }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onAddExpense, modifier = Modifier.weight(1f)) { Text("Add Expense") }
                OutlinedButton(onClick = onPayShare, modifier = Modifier.weight(1f)) { Text("Pay Share") }
            }
        }

        item { SectionHeader(title = "My Groups", actionLabel = "See all", onAction = onSeeAllGroups) }
        if (groups.isEmpty()) {
            item { EmptyText("You're not in any groups yet.") }
        }
        items(groups, key = { it.id }) { group ->
            GroupSummaryCard(group = group, onClick = { onGroupClick(group.id) })
        }

        item { SectionHeader(title = "Recent Activity", actionLabel = "See all", onAction = onSeeAllActivity) }
        if (uiState.recentActivity.isEmpty()) {
            item { EmptyText("No activity yet.") }
        } else item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column {
                    uiState.recentActivity.forEachIndexed { index, entry ->
                        if (index > 0) HorizontalDivider()
                        RecentActivityRow(entry = entry, onClick = onSeeAllActivity)
                    }
                }
            }
        }
    }
}

@Composable
private fun NetBalanceHeader(netBalanceCents: Long, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = when {
                netBalanceCents > 0 -> "You are owed (net)"
                netBalanceCents < 0 -> "You owe (net)"
                else -> "You're all settled up"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = formatMoney(abs(netBalanceCents)),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun RecentActivityRow(entry: ActivityEntry, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${entry.groupName} · ${entry.detail}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(text = formatMoney(entry.amountCents), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun EmptyText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun HomeContentPreview() {
    Payment_AppTheme() {
        Surface {
            HomeContent(
                uiState = HomeViewModel.buildState(FakeOverviewRepository(), hour = 9),
                groups = sampleGroups,
                onAddExpense = {}, onPayShare = {}, onSeeAllGroups = {}, onGroupClick = {},
                onSeeAllActivity = {}, onProfileClick = {},
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun HomeContentDarkPreview() {
    Payment_AppTheme() {
        Surface {
            HomeContent(
                uiState = HomeViewModel.buildState(FakeOverviewRepository(), hour = 20),
                groups = sampleGroups,
                onAddExpense = {}, onPayShare = {}, onSeeAllGroups = {}, onGroupClick = {},
                onSeeAllActivity = {}, onProfileClick = {},
            )
        }
    }
}
