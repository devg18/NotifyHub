package com.izooto.notifyhub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izooto.notifyhub.data.local.NotificationEntity
import com.izooto.notifyhub.ui.components.NotificationBannerImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationDetailScreen(
    notification: NotificationEntity?,
    onBack: () -> Unit,
    onMarkAsRead: () -> Unit
) {

    LaunchedEffect(notification?.id) {
        if (notification != null) {
            onMarkAsRead()
        }
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("‹  Back", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    "NOTIFICATION",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (notification == null) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Notification not found")
                }

            } else {

                NotificationBannerImage(
                    imageUrl = notification.bannerImg,
                    contentDescription = notification.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(22.dp))
                )

                Spacer(modifier = Modifier.height(25.dp))

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = if (notification.isRead) "✓ Read" else "Unread",
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 7.dp
                        ),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = notification.title,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = formatDetailDate(notification.timestamp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 22.dp)
                )

                Text(
                    "Message",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = notification.description.ifBlank {
                        "No description available."
                    },
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Notification ID",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Text(
                    notification.id,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatDetailDate(timestamp: Long): String {
    return try {
        SimpleDateFormat(
            "dd MMMM yyyy, hh:mm a",
            Locale.getDefault()
        ).format(Date(timestamp))
    } catch (e: Exception) {
        "Date unavailable"
    }
}
