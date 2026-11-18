package com.izooto.notifyhub.ui

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.izooto.notifyhub.ui.screens.NotificationDetailScreen
import com.izooto.notifyhub.ui.screens.NotificationInboxScreen
import com.izooto.notifyhub.ui.viewmodel.NotificationViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun NotifyHubNavigation(
    openInboxRequest: Int = 0,
    notificationViewModel: NotificationViewModel = viewModel()
) {
    val navController = rememberNavController()

    LaunchedEffect(openInboxRequest) {
        if (openInboxRequest > 0) {
            navController.navigate("inbox") {
                popUpTo("inbox") {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    val notifications by notificationViewModel.notifications
        .collectAsState()

    val unreadCount by notificationViewModel.unreadCount
        .collectAsState()

    NavHost(
        navController = navController,
        startDestination = "inbox"
    ) {
        composable("inbox") {
            NotificationInboxScreen(
                notifications = notifications,
                unreadCount = unreadCount,
                onNotificationClick = { notification ->
                    navController.navigate(
                        "details/${Uri.encode(notification.id)}"
                    )
                },
                onDeleteNotification = { id ->
                    notificationViewModel.deleteNotification(id)
                },
                onMarkAllAsRead = {
                    notificationViewModel.markAllAsRead()
                },
                onClearInbox = {
                    notificationViewModel.clearInbox()
                }
            )
        }

        composable(
            route = "details/{notificationId}",
            arguments = listOf(
                navArgument("notificationId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val notificationId =
                backStackEntry.arguments?.getString("notificationId")

            val notification = notifications.firstOrNull {
                it.id == notificationId
            }

            NotificationDetailScreen(
                notification = notification,
                onBack = {
                    navController.popBackStack()
                },
                onMarkAsRead = {
                    notificationId?.let {
                        notificationViewModel.markAsRead(it)
                    }
                }
            )
        }
    }
}
