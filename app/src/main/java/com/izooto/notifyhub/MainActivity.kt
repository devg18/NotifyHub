
package com.izooto.notifyhub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.messaging.FirebaseMessaging
import com.izooto.notifyhub.notification.NotificationHelper
import com.izooto.notifyhub.ui.NotifyHubNavigation
import com.izooto.notifyhub.ui.permissions.NotificationPermissionHandler
import com.izooto.notifyhub.ui.theme.NotifyHubTheme

class MainActivity : ComponentActivity() {

    private var inboxOpenRequest by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        handleNotificationIntent(intent)
        fetchFcmToken()

        setContent {
            NotifyHubTheme {
                NotificationPermissionHandler()

                NotifyHubNavigation(
                    openInboxRequest = inboxOpenRequest
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (
            intent?.getBooleanExtra(
                NotificationHelper.EXTRA_OPEN_INBOX,
                false
            ) == true
        ) {
            inboxOpenRequest++

            // Avoid processing the same intent again.
            intent.removeExtra(NotificationHelper.EXTRA_OPEN_INBOX)
            intent.removeExtra(NotificationHelper.EXTRA_NOTIFICATION_ID)

            Log.d("NotifyHubFCM", "Opening inbox from system notification")
        }
    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    Log.e(
                        "NotifyHubFCM",
                        "Failed to retrieve FCM token",
                        task.exception
                    )
                    return@addOnCompleteListener
                }

                Log.d(
                    "NotifyHubFCM",
                    "FCM registration token retrieved successfully"
                )
            }
    }
}
