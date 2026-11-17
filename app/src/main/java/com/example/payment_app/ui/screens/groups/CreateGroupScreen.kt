package com.example.payment_app.ui.screens.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Screen 08 · Create group */
@Composable
fun CreateGroupScreen(
    modifier: Modifier = Modifier,
    creatorName: String = "Alex Taylor",
    creatorUsername: String = "@alex",
    initialName: String = "",
    initialDescription: String = "",
    onBack: () -> Unit = {},
    onClose: () -> Unit = {},
    onContinue: (name: String, description: String) -> Unit = { _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }

    Column(modifier.fillMaxSize().background(GroupColors.Bg).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            RoundIconButton(Icons.AutoMirrored.Filled.ArrowBack, onBack)
            Text(
                "Create group", color = GroupColors.Text, style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )
            RoundIconButton(Icons.Default.Close, onClose)
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).clip(CircleShape).background(GroupColors.Card),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    creatorName.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").take(2),
                    color = GroupColors.Text, style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("You are the creator", color = GroupColors.Text, style = MaterialTheme.typography.titleSmall)
                Text("$creatorName $creatorUsername", color = GroupColors.Muted, style = MaterialTheme.typography.bodySmall)
            }
        }
        SectionLabel("Group name")
        AppTextField(name, { name = it }, "")
        SectionLabel("Description")
        AppTextField(description, { description = it }, "", singleLine = false, modifier = Modifier.height(140.dp))
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(GroupColors.Field).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$", color = GroupColors.Muted, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.width(8.dp))
            Text("Primary currency: USD", color = GroupColors.Muted, style = MaterialTheme.typography.labelMedium)
        }
        Spacer(Modifier.weight(1f))
        PillButton(
            "Continue to choose members", { onContinue(name, description) },
            Modifier.fillMaxWidth(), enabled = name.isNotBlank()
        )
    }
}

/** Screen 09 · Choose / add members */
@Composable
fun ChooseMembersScreen(
    modifier: Modifier = Modifier,
    selected: List<Member> = emptyList(),
    searchResults: List<Member> = emptyList(),
    onSearch: (String) -> Unit = {},
    onAdd: (Member) -> Unit = {},
    onRemove: (Member) -> Unit = {},
    onBack: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    Column(modifier.fillMaxSize().background(GroupColors.Bg).padding(16.dp)) {
        Text(
            "Add people by username", color = GroupColors.Muted, style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(12.dp))
        AppTextField(
            query, { query = it; onSearch(it) }, "Search by username",
            leading = { Icon(Icons.Default.Search, null, tint = GroupColors.Muted) }
        )
        LazyColumn(Modifier.weight(1f)) {
            item { SectionLabel("Selected members", "${selected.size}") }
            items(selected, key = { it.username }) { m ->
                ListRowCard(
                    title = m.name, subtitle = m.username,
                    trailing = {
                        if (m.isCreator) {
                            Text(
                                "Creator", color = GroupColors.Muted, style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                        Icon(
                            Icons.Default.Check, "Selected", tint = GroupColors.Text,
                            modifier = Modifier.size(22.dp)
                                .then(if (!m.isCreator) Modifier.clickable { onRemove(m) } else Modifier)
                        )
                    }
                )
            }
            item { SectionLabel("Search results", "${searchResults.size}") }
            items(searchResults, key = { it.username }) { m ->
                ListRowCard(
                    title = m.name, subtitle = m.username,
                    trailing = {
                        Icon(
                            Icons.Default.Add, "Add", tint = GroupColors.Text,
                            modifier = Modifier.size(22.dp).clickable { onAdd(m) }
                        )
                    }
                )
            }
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GroupColors.Muted, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Back", color = GroupColors.Muted)
            }
            Spacer(Modifier.weight(1f))
            PillButton("Finish", onFinish)
        }
    }
}

@Composable
private fun RoundIconButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        Modifier.size(36.dp).clip(CircleShape).background(GroupColors.Card).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Icon(icon, null, tint = GroupColors.Text, modifier = Modifier.size(18.dp)) }
}