package com.example.payment_app.data.repository

import com.example.payment_app.data.model.ActivityEntry
import com.example.payment_app.data.model.AppNotification

// NFR1 (offline access): data loaded while online stays available offline.
// Wraps another OverviewRepository: online -> fetch and remember a copy,
// offline -> return the last copy.
class CachedOverviewRepository(
    private val remote: OverviewRepository,
    private val connectivity: ConnectivityChecker,
) : OverviewRepository {

    private var cachedName: String = ""
    private var cachedActivity: List<ActivityEntry> = emptyList()
    private var cachedNotifications: List<AppNotification> = emptyList()

    override fun currentUserFirstName(): String {
        if (connectivity.isOnline()) cachedName = remote.currentUserFirstName()
        return cachedName
    }

    override fun activity(): List<ActivityEntry> {
        if (connectivity.isOnline()) cachedActivity = remote.activity()
        return cachedActivity
    }

    override fun notifications(): List<AppNotification> {
        if (connectivity.isOnline()) cachedNotifications = remote.notifications()
        return cachedNotifications
    }

    // The "Cached" badge shows whenever we are offline
    override fun isShowingCachedData(): Boolean = !connectivity.isOnline()
}