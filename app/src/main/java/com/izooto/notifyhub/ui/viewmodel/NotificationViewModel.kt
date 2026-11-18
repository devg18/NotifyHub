package com.izooto.notifyhub.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.izooto.notifyhub.data.local.NotificationDatabase
import com.izooto.notifyhub.data.repository.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        NotificationDatabase.getDatabase(application)

    private val repository =
        NotificationRepository(database.notificationDao())

    // All notifications, newest first
    val notifications = repository.allNotifications.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Number of unread notifications
    val unreadCount = repository.unreadCount.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    // Mark one notification as read
    fun markAsRead(id: String) {
        viewModelScope.launch {
            repository.markAsRead(id)
        }
    }

    // Mark all notifications as read
    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead()
        }
    }

    // Delete a notification
    fun deleteNotification(id: String) {
        viewModelScope.launch {
            repository.deleteNotification(id)
        }
    }

    // Clear the entire inbox
    fun clearInbox() {
        viewModelScope.launch {
            repository.clearInbox()
        }
    }
}
