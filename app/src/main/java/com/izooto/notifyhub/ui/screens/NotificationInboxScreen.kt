package com.izooto.notifyhub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izooto.notifyhub.data.local.NotificationEntity
import com.izooto.notifyhub.ui.components.NotificationBannerImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class InboxFilter {
    ALL, UNREAD, READ
}

@Composable
fun NotificationInboxScreen(
    notifications: List<NotificationEntity>,
    unreadCount: Int,
    onNotificationClick: (NotificationEntity) -> Unit,
    onDeleteNotification: (String) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onClearInbox: () -> Unit
) {
    var selectedFilter by remember {
        mutableStateOf(InboxFilter.ALL)
    }

    var showClearDialog by remember {
        mutableStateOf(false)
    }

    val filteredNotifications = when (selectedFilter) {
        InboxFilter.ALL -> notifications
        InboxFilter.UNREAD -> notifications.filter { !it.isRead }
        InboxFilter.READ -> notifications.filter { it.isRead }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "NotifyHub",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Your notification center",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔔", fontSize = 23.sp)
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Unread summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Unread notifications",
                            color = MaterialTheme.colorScheme.onPrimary.copy(
                                alpha = 0.85f
                            ),
                            fontSize = 14.sp
                        )

                        Text(
                            unreadCount.toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text("✉", fontSize = 34.sp)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Section heading and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Your inbox",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    TextButton(
                        onClick = onMarkAllAsRead,
                        enabled = unreadCount > 0
                    ) {
                        Text("Read all")
                    }

                    TextButton(
                        onClick = {
                            showClearDialog = true
                        },
                        enabled = notifications.isNotEmpty()
                    ) {
                        Text("Clear")
                    }
                }
            }

            // Filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == InboxFilter.ALL,
                    onClick = {
                        selectedFilter = InboxFilter.ALL
                    },
                    label = {
                        Text("All (${notifications.size})")
                    }
                )

                FilterChip(
                    selected = selectedFilter == InboxFilter.UNREAD,
                    onClick = {
                        selectedFilter = InboxFilter.UNREAD
                    },
                    label = {
                        Text("Unread")
                    }
                )

                FilterChip(
                    selected = selectedFilter == InboxFilter.READ,
                    onClick = {
                        selectedFilter = InboxFilter.READ
                    },
                    label = {
                        Text("Read")
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notification list
            if (filteredNotifications.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔔", fontSize = 48.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            when (selectedFilter) {
                                InboxFilter.ALL -> "Your inbox is empty"
                                InboxFilter.UNREAD -> "No unread notifications"
                                InboxFilter.READ -> "No read notifications"
                            },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        )

                        Text(
                            "New notifications will appear here.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        items = filteredNotifications,
                        key = { it.id }
                    ) { notification ->

                        NotificationCard(
                            notification = notification,
                            onClick = {
                                onNotificationClick(notification)
                            },
                            onDelete = {
                                onDeleteNotification(notification.id)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = {
                showClearDialog = false
            },
            title = {
                Text("Clear inbox?")
            },
            text = {
                Text("This will permanently delete all saved notifications.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearInbox()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear all")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showClearDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Banner image or fallback

// Notification banner with automatic fallback
            NotificationBannerImage(
                imageUrl = notification.bannerImg,
                contentDescription = notification.title,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(13.dp))
            )


            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = if (!notification.isRead)
                            FontWeight.Bold else FontWeight.Medium,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = notification.description.ifBlank {
                        "No description available"
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = formatNotificationDate(notification.timestamp),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            TextButton(
                onClick = onDelete,
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(
                    "×",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatNotificationDate(timestamp: Long): String {
    return try {
        SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        ).format(Date(timestamp))
    } catch (e: Exception) {
        "Date unavailable"
    }
}
