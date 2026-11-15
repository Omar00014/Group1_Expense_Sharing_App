package com.example.payment_app.data.repository

import com.example.payment_app.data.model.ActivityEntry
import com.example.payment_app.data.model.ActivityType
import com.example.payment_app.data.model.AppNotification
import com.example.payment_app.data.model.GroupSummary
import com.example.payment_app.data.model.NotificationKind

interface OverviewRepository {
    fun currentUserFirstName(): String
    fun groups(): List<GroupSummary>
    fun activity(): List<ActivityEntry>
    fun notifications(): List<AppNotification>

    fun isShowingCachedData(): Boolean
}

class FakeOverviewRepository : OverviewRepository {

    override fun currentUserFirstName(): String = "Alex"

    override fun groups(): List<GroupSummary> = listOf(
        GroupSummary(id = "weekend-trip", name = "Weekend Trip", memberCount = 4, expenseCount = 3, balanceCents = 6_200),
        GroupSummary(id = "apartment", name = "Apartment", memberCount = 3, expenseCount = 7, balanceCents = -1_450),
    )

    override fun activity(): List<ActivityEntry> = listOf(
        ActivityEntry(
            id = "a1", type = ActivityType.EXPENSE, actorName = "Jordan", title = "Jordan paid Dinner",
            groupId = "weekend-trip", groupName = "Weekend Trip", detail = "split 4 ways",
            amountCents = 8_800, dateLabel = "Today",
        ),
        ActivityEntry(
            id = "a2", type = ActivityType.EXPENSE, actorName = "You", title = "You paid Groceries",
            groupId = "apartment", groupName = "Apartment", detail = "split 3 ways",
            amountCents = 4_350, dateLabel = "Yesterday",
        ),
        ActivityEntry(
            id = "a3", type = ActivityType.SETTLEMENT, actorName = "Sam", title = "Sam settled up with you",
            groupId = "weekend-trip", groupName = "Weekend Trip", detail = "settled",
            amountCents = 2_200, dateLabel = "2d ago",
        ),
        ActivityEntry(
            id = "a4", type = ActivityType.EXPENSE, actorName = "Maya", title = "Maya paid Internet",
            groupId = "apartment", groupName = "Apartment", detail = "split 3 ways",
            amountCents = 3_000, dateLabel = "3d ago",
        ),
        ActivityEntry(
            id = "a5", type = ActivityType.EXPENSE, actorName = "You", title = "You paid Fuel",
            groupId = "weekend-trip", groupName = "Weekend Trip", detail = "split 4 ways",
            amountCents = 6_400, dateLabel = "4d ago",
        ),
    )

    override fun notifications(): List<AppNotification> = listOf(
        AppNotification(
            id = "n1", kind = NotificationKind.NEW_EXPENSE, title = "Jordan added Dinner",
            subtitle = "Weekend Trip · $88.00", amountCents = 8_800, timeLabel = "2m ago", isRead = false,
        ),
        AppNotification(
            id = "n2", kind = NotificationKind.PAYMENT_DUE, title = "Pay Maya",
            subtitle = "Apartment · $14.50", amountCents = 1_450, timeLabel = "Today", isRead = false,
        ),
        AppNotification(
            id = "n3", kind = NotificationKind.SETTLEMENT, title = "Sam settled with you",
            subtitle = "Weekend Trip · 2d ago", amountCents = 2_200, timeLabel = "2d ago", isRead = true,
        ),
        AppNotification(
            id = "n4", kind = NotificationKind.SYNC, title = "Groceries synced successfully",
            subtitle = "Apartment · Saved after reconnecting", amountCents = null, timeLabel = "Yesterday", isRead = true,
        ),
        AppNotification(
            id = "n5", kind = NotificationKind.SETTLEMENT, title = "Emma settled with you",
            subtitle = "Coffee Run · 3d ago", amountCents = 3_100, timeLabel = "3d ago", isRead = true,
        ),
        AppNotification(
            id = "n6", kind = NotificationKind.SYNC, title = "Weekend Trip synced",
            subtitle = "Weekend Trip · 4d ago", amountCents = null, timeLabel = "4d ago", isRead = true,
        ),
    )

    override fun isShowingCachedData(): Boolean = true
}
