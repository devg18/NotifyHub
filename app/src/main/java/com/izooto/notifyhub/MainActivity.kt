package com.izooto.notifyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.izooto.notifyhub.ui.theme.NotifyHubTheme
import com.izooto.notifyhub.ui.permissions.NotificationPermissionHandler
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fetchFcmToken()

        setContent {
            NotifyHubTheme {

                NotificationPermissionHandler()
            }
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

                val token = task.result

                Log.d(
                    "NotifyHubFCM",
                    "FCM Registration Token: $token"
                )
            }
    }

}
