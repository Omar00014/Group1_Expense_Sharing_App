package com.example.payment_app.data.remote

import kotlinx.coroutines.delay
import java.io.IOException

interface NotificationsRemote {
    suspend fun markAsRead(notificationIds: List<String>)
}

class FakeNotificationsRemote(
    private val networkStatus: NetworkStatus,
    private val latencyMs: Long = 500,
) : NotificationsRemote {

    override suspend fun markAsRead(notificationIds: List<String>) {
        delay(latencyMs)
        if (!networkStatus.isOnline()) throw IOException("No network connection")
    }
}
