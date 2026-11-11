package com.example.payment_app.ui.screens

import com.example.payment_app.ui.viewmodel.NotificationsUiState
import com.example.payment_app.ui.viewmodel.NotificationsViewModel

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.data.model.AppNotification
import com.example.payment_app.data.model.NotificationKind
import com.example.payment_app.ui.components.InitialAvatar
import com.example.payment_app.ui.components.SectionHeader
import com.example.payment_app.ui.components.formatMoney
import com.example.payment_app.ui.theme.Payment_AppTheme

/** Screen 06 · Notifications, connected to its ViewModel. Use this from navigation. */
@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onNotificationClick: (AppNotification) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotificationsContent(
        uiState = uiState,
        onBack = onBack,
        onMarkAllAsRead = viewModel::markAllAsRead,
        onOpenSettings = onOpenSettings,
        onNotificationClick = { notification ->
            viewModel.markAsRead(notification.id)
            onNotificationClick(notification)
        },
        modifier = modifier,
    )
}

/** Stateless UI for screen 06 · Notifications. */
@Composable
fun NotificationsContent(
    uiState: NotificationsUiState,
    onBack: () -> Unit,
    onMarkAllAsRead: () -> Unit,
    onOpenSettings: () -> Unit,
    onNotificationClick: (AppNotification) -> Unit,
    modifier: Modifier = Modifier,
) {
    val unread = uiState.unread
    val read = uiState.read

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                )
                InitialAvatar(name = uiState.userFirstName)
            }
        }

        item {
            Text(
                text = when (unread.size) {
                    0 -> "You're all caught up"
                    1 -> "1 unread update"
                    else -> "${unread.size} unread updates"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            Button(
                onClick = onMarkAllAsRead,
                enabled = unread.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Mark all as read") }
        }

        item { SectionHeader(title = "Recent (${unread.size})", actionLabel = "Settings", onAction = onOpenSettings) }
        if (unread.isEmpty()) {
            item {
                Text(
                    text = "No new notifications.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        items(unread, key = { it.id }) { notification ->
            UnreadNotificationCard(notification = notification, onClick = { onNotificationClick(notification) })
        }

        if (read.isNotEmpty()) {
            item { SectionHeader(title = "Read (${read.size})") }
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column {
                        read.forEachIndexed { index, notification ->
                            if (index > 0) HorizontalDivider()
                            ReadNotificationRow(
                                notification = notification,
                                onClick = { onNotificationClick(notification) },
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Badge text for unread notifications, e.g. "NEW" or "DUE". */
private fun badgeLabel(kind: NotificationKind): String = when (kind) {
    NotificationKind.PAYMENT_DUE -> "DUE"
    else -> "NEW"
}

/** Right-hand text for read notifications: the amount, or "Background" for sync events. */
private fun trailingLabel(notification: AppNotification): String =
    if (notification.kind == NotificationKind.SYNC) "Background"
    else notification.amountCents?.let { formatMoney(it) } ?: ""

@Composable
private fun UnreadNotificationCard(notification: AppNotification, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val isDue = notification.kind == NotificationKind.PAYMENT_DUE
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InitialAvatar(name = notification.title)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = notification.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Badge(
                    containerColor = if (isDue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    contentColor = if (isDue) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary,
                ) {
                    Text(badgeLabel(notification.kind), modifier = Modifier.padding(horizontal = 2.dp))
                }
                Text(
                    text = notification.timeLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}

@Composable
private fun ReadNotificationRow(notification: AppNotification, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = notification.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = trailingLabel(notification),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun previewState() = FakeOverviewRepository().let {
    NotificationsUiState(userFirstName = it.currentUserFirstName(), notifications = it.notifications())
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun NotificationsContentPreview() {
    Payment_AppTheme(dynamicColor = false) {
        Surface {
            NotificationsContent(
                uiState = previewState(),
                onBack = {}, onMarkAllAsRead = {}, onOpenSettings = {}, onNotificationClick = {},
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun NotificationsAllReadPreview() {
    Payment_AppTheme(darkTheme = true, dynamicColor = false) {
        Surface {
            val state = previewState()
            NotificationsContent(
                uiState = state.copy(notifications = state.notifications.map { it.copy(isRead = true) }),
                onBack = {}, onMarkAllAsRead = {}, onOpenSettings = {}, onNotificationClick = {},
            )
        }
    }
}
