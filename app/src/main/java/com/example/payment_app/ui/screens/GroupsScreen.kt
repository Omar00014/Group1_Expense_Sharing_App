package com.example.payment_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.payment_app.ui.screens.groups.*
import com.example.payment_app.ui.theme.Payment_AppTheme
import kotlin.collections.filter


@Composable
fun GroupsScreen(
    modifier: Modifier = Modifier,
    groups: List<GroupSummary> = sampleGroups,
    onCreateGroup: () -> Unit = {},
    onGroupClick: (GroupSummary) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val filtered = groups.filter { it.name.contains(query, ignoreCase = true) }
    val net = groups.sumOf { it.amount }

    Column(
        modifier.fillMaxSize().background(GroupColors.Bg).padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        BigHeader("You are owed (net)", formatMoney(net, signed = false))
        AppTextField(
            value = query, onValueChange = { query = it }, placeholder = "Search groups...",
            leading = { Icon(Icons.Default.Search, null, tint = GroupColors.Muted) }
        )
        Spacer(Modifier.height(12.dp))
        PillButton("Create Group", onCreateGroup, Modifier.fillMaxWidth())
        SectionLabel("My Groups", "${filtered.size} groups")
        LazyColumn {
            items(filtered, key = { it.id }) { g ->
                ListRowCard(
                    title = g.name,
                    subtitle = g.subtitle,
                    endTitle = formatMoney(g.amount),
                    endCaption = if (g.amount >= 0) "owed to you" else "you owe",
                    onClick = { onGroupClick(g) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupsScreenPreview() {
    Payment_AppTheme { GroupsScreen() }
}