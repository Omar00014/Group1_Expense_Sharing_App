package com.example.payment_app.data.model

enum class ActivityType { EXPENSE, SETTLEMENT }

data class ActivityEntry(
    val id: String,
    val type: ActivityType,
    val actorName: String,
    val title: String,
    val groupId: String,
    val groupName: String,
    val detail: String,
    val amountCents: Long,
    val dateLabel: String,
)

enum class NotificationKind { NEW_EXPENSE, PAYMENT_DUE, SETTLEMENT, SYNC }

data class AppNotification(
    val id: String,
    val kind: NotificationKind,
    val title: String,
    val subtitle: String,
    val amountCents: Long?,
    val timeLabel: String,
    val isRead: Boolean,
)
