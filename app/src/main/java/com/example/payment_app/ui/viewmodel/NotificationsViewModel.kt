package com.example.payment_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.payment_app.data.model.AppNotification
import com.example.payment_app.data.remote.FakeNotificationsRemote
import com.example.payment_app.data.remote.NotificationsRemote
import com.example.payment_app.data.remote.safeRequest
import com.example.payment_app.data.repository.FakeOverviewRepository
import com.example.payment_app.data.repository.OverviewRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val userFirstName: String = "",
    val notifications: List<AppNotification> = emptyList(),
    val errorMessage: String? = null,
) {
    val unread: List<AppNotification> get() = notifications.filterNot { it.isRead }
    val read: List<AppNotification> get() = notifications.filter { it.isRead }
}

class NotificationsViewModel(
    repository: OverviewRepository = FakeOverviewRepository(),
    private val remote: NotificationsRemote = FakeNotificationsRemote({ true }),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NotificationsUiState(
            userFirstName = repository.currentUserFirstName(),
            notifications = repository.notifications(),
        )
    )
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun markAllAsRead() {
        val ids = _uiState.value.unread.map { it.id }
        if (ids.isEmpty()) return
        _uiState.update { state ->
            state.copy(notifications = state.notifications.map { it.copy(isRead = true) })
        }
        sendToServer(ids)
    }

    fun markAsRead(notificationId: String) {
        val notification = _uiState.value.notifications.firstOrNull { it.id == notificationId } ?: return
        if (notification.isRead) return
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map {
                    if (it.id == notificationId) it.copy(isRead = true) else it
                }
            )
        }
        sendToServer(listOf(notificationId))
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun sendToServer(ids: List<String>) {
        viewModelScope.launch {
            safeRequest(ioDispatcher) { remote.markAsRead(ids) }
                .onFailure { _uiState.update { it.copy(errorMessage = REQUEST_FAILED_MESSAGE) } }
        }
    }

    companion object {
        const val REQUEST_FAILED_MESSAGE = "No connection. Couldn't update the server, but you can keep using the app."
    }
}
