package com.izooto.notifyhub.data.repository

import com.izooto.notifyhub.data.local.NotificationDao
import com.izooto.notifyhub.data.local.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(
    private val notificationDao: NotificationDao
) {

    val allNotifications: Flow<List<NotificationEntity>> =
        notificationDao.getAllNotifications()

    val unreadCount: Flow<Int> =
        notificationDao.getUnreadCount()

    suspend fun saveNotification(
        notification: NotificationEntity
    ): Boolean {

        return notificationDao.insertNotification(notification) != -1L
    }

    suspend fun getNotification(
        id: String
    ): NotificationEntity? {

        return notificationDao.getNotificationById(id)
    }

    suspend fun markAsRead(id: String) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
    }

    suspend fun deleteNotification(id: String) {
        notificationDao.deleteNotification(id)
    }

    suspend fun clearInbox() {
        notificationDao.clearInbox()
    }
}