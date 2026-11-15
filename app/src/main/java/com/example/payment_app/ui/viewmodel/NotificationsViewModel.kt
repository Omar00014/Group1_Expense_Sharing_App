package com.example.payment_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.data.repository.OverviewRepository
import com.example.payment_app.data.model.AppNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotificationsUiState(
    val userFirstName: String = "",
    val notifications: List<AppNotification> = emptyList(),
) {
    val unread: List<AppNotification> get() = notifications.filterNot { it.isRead }
    val read: List<AppNotification> get() = notifications.filter { it.isRead }
}

class NotificationsViewModel(
    repository: OverviewRepository = FakeOverviewRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NotificationsUiState(
            userFirstName = repository.currentUserFirstName(),
            notifications = repository.notifications(),
        )
    )
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun markAllAsRead() {
        _uiState.update { state ->
            state.copy(notifications = state.notifications.map { it.copy(isRead = true) })
        }
    }

    fun markAsRead(notificationId: String) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map {
                    if (it.id == notificationId) it.copy(isRead = true) else it
                }
            )
        }
    }
}
