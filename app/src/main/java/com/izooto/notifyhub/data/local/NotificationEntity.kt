package com.izooto.notifyhub.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(

    @PrimaryKey
    val id: String,

    val title: String,

    val description: String,

    val bannerImg: String?,

    val timestamp: Long,

    val isRead: Boolean = false
)