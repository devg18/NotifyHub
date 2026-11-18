package com.izooto.notifyhub.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.izooto.notifyhub.data.local.NotificationDatabase
import com.izooto.notifyhub.data.local.NotificationEntity
import com.izooto.notifyhub.data.repository.NotificationRepository
import com.izooto.notifyhub.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import android.net.Uri
import java.util.UUID

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Token is refreshed when Firebase rotates it.
        // We will configure token retrieval for testing separately.
        Log.d("NotifyHubFCM", "FCM registration token refreshed")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        try {
            val data = remoteMessage.data

            val notificationId = data["id"]
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?: UUID.randomUUID().toString()

            val title = data["title"]
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?: remoteMessage.notification?.title
                    ?.takeIf { it.isNotBlank() }
                ?: "New Notification"

            val description = data["description"]
                ?: remoteMessage.notification?.body
                ?: ""

            val bannerImg = data["bannerImg"]
                ?.trim()
                ?.takeIf { url ->
                    url.isNotEmpty() &&
                            runCatching {
                                val uri = Uri.parse(url)
                                uri.scheme in listOf("http", "https") &&
                                        !uri.host.isNullOrBlank()
                            }.getOrDefault(false)
                }

            val timestamp = data["timestamp"]
                ?.toLongOrNull()
                ?.takeIf { it > 0L }
                ?: System.currentTimeMillis()


            val notification = NotificationEntity(
                id = notificationId,
                title = title,
                description = description,
                bannerImg = bannerImg,
                timestamp = timestamp,
                isRead = false
            )

            // Save locally before showing the system notification.
            val database = NotificationDatabase.getDatabase(
                applicationContext
            )

            val repository = NotificationRepository(
                database.notificationDao()
            )

            val inserted = runBlocking(Dispatchers.IO) {
                repository.saveNotification(notification)
            }

            // Do not display duplicate system notifications.
            if (inserted) {

                NotificationHelper.showNotification(
                    context = applicationContext,
                    id = notificationId,
                    title = title,
                    description = description
                )

                Log.d(
                    "NotifyHubFCM",
                    "Notification saved successfully: $notificationId"
                )

            } else {

                Log.d(
                    "NotifyHubFCM",
                    "Duplicate notification ignored: $notificationId"
                )
            }

        } catch (exception: Exception) {

            Log.e(
                "NotifyHubFCM",
                "Failed to process notification",
                exception
            )
        }
    }
}
