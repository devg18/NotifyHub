# NotifyHub — Android Push Notification Inbox

NotifyHub is a native Android application developed as part of an Android Developer assignment for Izoto. It integrates Firebase Cloud Messaging (FCM) with a locally maintained notification inbox using Room Database.

The application receives push notifications, displays Android system notifications, stores notification history locally, and allows users to view and manage their notifications.

## Features

- Receive push notifications through Firebase Cloud Messaging.
- Display Android system notifications using the `assignment` notification channel.
- Request notification permission on Android 13 and newer.
- Save notification history locally using Room Database.
- Display notification title, description, banner image and timestamp.
- Maintain Read and Unread notification status.
- Prevent duplicate records using notification IDs.
- Open the notification inbox when a system notification is tapped.
- View individual notification details.
- Automatically mark notifications as Read when opened.
- Filter notifications by All, Unread and Read.
- Display the unread notification count.
- Mark all notifications as Read.
- Delete individual notifications or clear the inbox.
- Preserve notification history after the application is closed or restarted.
- Handle missing or invalid banner image URLs with a fallback UI.

## Technology Stack

| Technology | Purpose |
|---|---|
| Kotlin | Application development |
| Jetpack Compose | User interface |
| Material 3 | UI components and styling |
| MVVM | Application architecture |
| Room Database | Local notification storage |
| Firebase Cloud Messaging | Push notification delivery |
| Navigation Compose | Screen navigation |
| Coil | Banner image loading |
| Kotlin Coroutines and Flow | Asynchronous database operations and data observation |

## Application Package

`com.izooto.notifyhub`

## Project Structure

```text
NotifyHub/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/izooto/notifyhub/
│   │       │   ├── data/
│   │       │   │   ├── local/
│   │       │   │   └── repository/
│   │       │   ├── notification/
│   │       │   ├── service/
│   │       │   ├── ui/
│   │       │   │   ├── components/
│   │       │   │   ├── permissions/
│   │       │   │   ├── screens/
│   │       │   │   └── viewmodel/
│   │       │   ├── MainActivity.kt
│   │       │   └── NotifyHubApp.kt
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/devg18/NotifyHub.git
```

Open the project in Android Studio.

### 2. Configure Firebase

1. Open the Firebase Console.
2. Create or select a Firebase project.
3. Register an Android application with package name:

   `com.izooto.notifyhub`

4. Download `google-services.json`.
5. Place the file inside the `app/` directory.
6. Enable Firebase Cloud Messaging for the project.
7. Sync the project with Gradle.

**Important:** `google-services.json` is intentionally excluded from this repository. Obtain the configuration file from the Firebase project owner or configure your own Firebase project.

### 3. Build the application

On Windows PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
```

The generated debug APK is located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## FCM Message Payload

The application expects a data payload containing these fields:

```json
{
  "id": "notification_101",
  "title": "New Notification",
  "description": "This is a sample notification for the assignment.",
  "bannerImg": "https://example.com/banner.jpg",
  "timestamp": "1791873000000"
}
```

Field descriptions:

| Field | Description |
|---|---|
| id | Unique notification identifier |
| title | Notification title |
| description | Notification message |
| bannerImg | Optional HTTP/HTTPS banner image URL |
| timestamp | Unix timestamp in milliseconds |

A stable notification ID should be supplied by the sender to support duplicate prevention.

For reliable background processing through `FirebaseMessagingService`, send a data-only FCM message. A notification-plus-data message may be automatically displayed by Android when the app is in the background, without following the same service-processing path.

### Sending test messages

Use a trusted server or Firebase Admin SDK to send data-only FCM messages.

Do not put Firebase service-account private keys inside the Android application or commit them to GitHub.

## Notification Channel

| Property | Value |
|---|---|
| Channel ID | `assignment` |
| Channel Name | `Assignment` |
| Importance | Default |

Android 13 and newer require notification permission before system notifications can be displayed.

## Local Database

Room Database stores:

- Notification ID
- Title
- Description
- Banner image URL
- Timestamp
- Read/Unread status

The notification history is stored locally and remains available after the app is restarted.

## Testing Performed

- Firebase data-only message reception.
- Background notification delivery.
- System notification tap navigation.
- Cold-start and already-running navigation.
- Notification persistence after app restart.
- Inbox All, Unread and Read filters.
- Automatic Read status update.
- Unread count updates.
- Individual notification deletion.
- Clear inbox functionality.
- Duplicate notification ID handling.
- Banner image and fallback handling.

## Security Notes

- Do not commit Firebase service-account credentials.
- Do not commit private signing keys or keystore passwords.
- Keep local Firebase configuration out of public Git history.
- Send FCM messages through a trusted backend or sender.

## Author

Developed as an Android Developer assignment project for Izoto.

**Project:** NotifyHub  
**Platform:** Android  
**Language:** Kotlin  
**UI:** Jetpack Compose