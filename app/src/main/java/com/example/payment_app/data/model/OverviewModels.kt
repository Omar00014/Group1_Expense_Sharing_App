package com.example.payment_app.data.model

/**
 * Data shown on the Overview & activity pages (Home, Transaction history, Notifications).
 *
 * Money is stored as whole cents in a [Long] to avoid floating point rounding errors.
 * A positive balance means the current user is owed money, a negative one means they owe.
 */
data class GroupSummary(
    val id: String,
    val name: String,
    val memberCount: Int,
    val expenseCount: Int,
    val balanceCents: Long,
)

enum class ActivityType { EXPENSE, SETTLEMENT }

data class ActivityEntry(
    val id: String,
    val type: ActivityType,
    /** Who paid or settled, e.g. "Jordan" or "You". */
    val actorName: String,
    /** Full sentence shown as the row title, e.g. "Jordan paid Dinner". */
    val title: String,
    val groupId: String,
    val groupName: String,
    /** Extra info after the group name, e.g. "split 4 ways". */
    val detail: String,
    val amountCents: Long,
    /** Already formatted relative date, e.g. "Today" or "2d ago". */
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
