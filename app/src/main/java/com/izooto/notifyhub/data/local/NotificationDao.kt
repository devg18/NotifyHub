package com.izooto.notifyhub.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    // Insert notification without creating duplicates
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(
        notification: NotificationEntity
    ): Long

    // Observe all notifications
    @Query("""
        SELECT * FROM notifications
        ORDER BY timestamp DESC
    """)
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    // Get one notification by ID
    @Query("""
        SELECT * FROM notifications
        WHERE id = :notificationId
    """)
    suspend fun getNotificationById(
        notificationId: String
    ): NotificationEntity?

    // Observe unread notification count
    @Query("""
        SELECT COUNT(*) FROM notifications
        WHERE isRead = 0
    """)
    fun getUnreadCount(): Flow<Int>

    // Mark a notification as read
    @Query("""
        UPDATE notifications
        SET isRead = 1
        WHERE id = :notificationId
    """)
    suspend fun markAsRead(notificationId: String)

    // Mark all notifications as read
    @Query("""
        UPDATE notifications
        SET isRead = 1
    """)
    suspend fun markAllAsRead()

    // Delete one notification
    @Query("""
        DELETE FROM notifications
        WHERE id = :notificationId
    """)
    suspend fun deleteNotification(notificationId: String)

    // Clear complete inbox
    @Query("DELETE FROM notifications")
    suspend fun clearInbox()
}