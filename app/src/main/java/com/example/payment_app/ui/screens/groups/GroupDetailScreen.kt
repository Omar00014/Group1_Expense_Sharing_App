package com.example.payment_app.ui.screens.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Screen 10 · Group overview */
@Composable
fun GroupOverviewScreen(
    group: GroupSummary,
    members: List<Member>,
    activity: List<ActivityItem>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onAddExpense: () -> Unit = {},
    onViewBalances: () -> Unit = {},
    onManageMembers: () -> Unit = {},
    onSeeAllActivity: () -> Unit = {}
) {
    Column(modifier.fillMaxSize().background(GroupColors.Bg).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            BackIcon(onBack)
            Text(group.name, color = GroupColors.Muted, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            Box(
                Modifier.clip(CircleShape).background(GroupColors.Pill).clickable { onEdit() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) { Text("Edit", color = GroupColors.OnPill, style = MaterialTheme.typography.labelSmall) }
        }
        BigHeader(
            if (group.amount >= 0) "You are owed (net)" else "You owe (net)",
            formatMoney(group.amount, signed = false)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PillButton("Add Expense", onAddExpense, Modifier.weight(1f))
            PillButton("View Balances", onViewBalances, Modifier.weight(1f))
        }
        LazyColumn(Modifier.weight(1f)) {
            item { SectionLabel("Members · ${members.size}", "Manage", onManageMembers) }
            items(members, key = { it.username }) { m ->
                ListRowCard(
                    title = m.name, subtitle = m.note,
                    endTitle = formatMoney(m.balance),
                    endCaption = if (m.balance >= 0) "owed to you" else "owes you"
                )
            }
            item { SectionLabel("Recent activity", "See all", onSeeAllActivity) }
            items(activity) { a ->
                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(a.title, color = GroupColors.Text, style = MaterialTheme.typography.bodyMedium)
                        Text(a.subtitle, color = GroupColors.Muted, style = MaterialTheme.typography.labelSmall)
                    }
                    Text(formatMoney(a.amount, signed = false), color = GroupColors.Text, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

/** Screen 11 · Edit group / name / leave */
@Composable
fun EditGroupScreen(
    group: GroupSummary,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSave: (name: String, description: String) -> Unit = { _, _ -> },
    onManageMembers: () -> Unit = {},
    onKeepGroup: () -> Unit = {},
    onConfirmLeave: () -> Unit = {}
) {
    var name by remember { mutableStateOf(group.name) }
    var description by remember { mutableStateOf(group.description) }

    Column(modifier.fillMaxSize().background(GroupColors.Bg).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackIcon(onBack)
            Text("Edit ${group.name}", color = GroupColors.Muted, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Group details", color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            name.ifBlank { "Group name" }, color = GroupColors.Text, style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PillButton("Save Changes", { onSave(name, description) }, Modifier.weight(1f))
            PillButton("Manage Members", onManageMembers, Modifier.weight(1f))
        }
        SectionLabel("Group Name")
        AppTextField(name, { name = it }, "Group name")
        SectionLabel("Description")
        AppTextField(description, { description = it }, "Description", singleLine = false)
        SectionLabel("Group Stats")
        Text(group.subtitle, color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.weight(1f))
        Text(
            "Leave ${group.name}?", color = GroupColors.Text, style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            "Leaving removes access to this group.\nSettle outstanding balances before leaving.",
            color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PillButton("Keep group", onKeepGroup, Modifier.weight(1f))
            PillButton("Confirm leave", onConfirmLeave, Modifier.weight(1f), filled = false)
        }
        Text(
            "Confirming is required before leaving.", color = GroupColors.Muted,
            style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}

/** Screen 12 · Manage / remove members */
@Composable
fun ManageMembersScreen(
    group: GroupSummary,
    members: List<Member>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onAddMember: () -> Unit = {},
    onSearch: () -> Unit = {},
    onRemoveMember: (Member) -> Unit = {}
) {
    Column(modifier.fillMaxSize().background(GroupColors.Bg).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackIcon(onBack)
            Text(group.name, color = GroupColors.Muted, style = MaterialTheme.typography.bodyMedium)
        }
        BigHeader("Manage members", "${members.size} members")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PillButton("Add member", onAddMember, Modifier.weight(1f))
            PillButton("Search", onSearch, Modifier.weight(1f))
        }
        LazyColumn(Modifier.weight(1f)) {
            item { SectionLabel("Members", "${members.size} total") }
            items(members, key = { it.username }) { m ->
                val canRemove = !m.isYou && m.balance == 0.0
                ListRowCard(
                    title = m.name,
                    subtitle = if (m.isYou) "${m.username} · you" else m.username,
                    endTitle = formatMoney(m.balance),
                    endCaption = when {
                        m.isYou -> "You"
                        canRemove -> "Remove"
                        else -> "Settle first"
                    },
                    endCaptionUnderlined = canRemove,
                    onEndCaptionClick = if (canRemove) ({ onRemoveMember(m) }) else null
                )
            }
        }
        Text("Remove only after outstanding debt is settled.", color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall)
    }
}
@Composable
private fun BackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = GroupColors.Text)
    }
}