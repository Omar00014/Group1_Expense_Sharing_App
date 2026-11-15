package com.example.payment_app.ui.screens.profile

import androidx.compose.runtime.saveable.listSaver

enum class Currency(val code: String, val symbol: String) {
    USD("USD", "$"),
    EUR("EUR", "€")
}

data class UserProfile(
    val fullName: String,
    val username: String,
    val email: String,
    val currency: Currency
)

data class NotificationSettings(
    val newExpensePosted: Boolean = true,
    val expenseUpdates: Boolean = true,
    val paymentReminders: Boolean = true,
    val pushNotifications: Boolean = true,
    val emailSummaries: Boolean = false,
    val backgroundSync: Boolean = true
)

val UserProfileSaver = listSaver<UserProfile, String>(
    save = { listOf(it.fullName, it.username, it.email, it.currency.name) },
    restore = { UserProfile(it[0], it[1], it[2], Currency.valueOf(it[3])) }
)

val NotificationSettingsSaver = listSaver<NotificationSettings, Boolean>(
    save = {
        listOf(
            it.newExpensePosted, it.expenseUpdates, it.paymentReminders,
            it.pushNotifications, it.emailSummaries, it.backgroundSync
        )
    },
    restore = {
        NotificationSettings(it[0], it[1], it[2], it[3], it[4], it[5])
    }
)