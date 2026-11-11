package com.example.payment_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.payment_app.data.model.GroupSummary
import com.example.payment_app.ui.theme.Payment_AppTheme

/** Round avatar showing the first letter of [name]. Stands in for profile pictures for now. */
@Composable
fun InitialAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

/** Section title with an optional action on the right, e.g. "My Groups ..... See all". */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        if (actionLabel != null) {
            TextButton(onClick = onAction) { Text(actionLabel) }
        }
    }
}

/** Colour for a balance: positive (owed to you) uses primary, negative (you owe) uses error. */
@Composable
fun balanceColor(cents: Long): Color = when {
    cents > 0 -> MaterialTheme.colorScheme.primary
    cents < 0 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}

/** "owed to you", "you owe" or "settled", shown under a balance. */
fun balanceCaption(cents: Long): String = when {
    cents > 0 -> "owed to you"
    cents < 0 -> "you owe"
    else -> "settled"
}

/** Card for one group on the Home page. */
@Composable
fun GroupSummaryCard(
    group: GroupSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InitialAvatar(name = group.name)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${countLabel(group.memberCount, "member")} · ${countLabel(group.expenseCount, "expense")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
                Text(
                    text = formatMoney(group.balanceCents, showPlusSign = true),
                    style = MaterialTheme.typography.titleSmall,
                    color = balanceColor(group.balanceCents),
                )
                Text(
                    text = balanceCaption(group.balanceCents),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupSummaryCardPreview() {
    Payment_AppTheme(dynamicColor = false) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader(title = "My Groups", actionLabel = "See all")
            GroupSummaryCard(
                group = GroupSummary("weekend-trip", "Weekend Trip", 4, 3, 6_200),
                onClick = {},
            )
            GroupSummaryCard(
                group = GroupSummary("apartment", "Apartment", 3, 7, -1_450),
                onClick = {},
            )
        }
    }
}
