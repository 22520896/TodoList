package com.example.todolist.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.todolist.FocusActivity
import com.example.todolist.MainActivity
import com.example.todolist.R

class ReminderReceiver : BroadcastReceiver() {

    @Suppress("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {

        // 1. Lấy dữ liệu
        val todoId          = intent.getLongExtra("todoId", -1L)
        val title           = intent.getStringExtra("title") ?: "Nhắc nhở"
        val dateStr         = intent.getStringExtra("date") ?: ""
        val startTimeStr    = intent.getStringExtra("start_time") ?: ""
        val endTimeStr      = intent.getStringExtra("end_time") ?: ""
        val rawFocusFlag    = intent.getBooleanExtra("isFocusEnabled", false)

        val focusEnabled    = rawFocusFlag && (todoId != -1L) && (!FocusSessionManager.isRunning() || FocusSessionManager.currentId() == todoId)

        // 2. Chuông người dùng chọn
        val prefs       = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        val ringtoneUri = prefs.getString("ringtone_uri", null)?.let { Uri.parse(it) }

        // 3. Intent mở Activity
        val targetCls = if (focusEnabled) FocusActivity::class.java else MainActivity::class.java

        val openIntent = Intent(context, targetCls).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("todoId", todoId)
        }

        val openPending = PendingIntent.getActivity(
            context,
            todoId.toInt(),
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (focusEnabled) {
            FocusSessionManager.start(todoId)
            context.startActivity(openIntent)
        }

        // 4. Notification
        val channelId = "todo_reminder_channel"
        val nm        = context.getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            nm.getNotificationChannel(channelId) == null
        ) {
            nm.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Nhắc nhở Todo",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableVibration(true)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
            )
        }

        val bigText = "$title\n$dateStr • $startTimeStr → $endTimeStr"

        // 5. Full‑screen notification
        val notif = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle("🔔 Nhắc nhở nhiệm vụ")
            .setContentText(bigText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setContentIntent(openPending)
            .setAutoCancel(true)
            .setSound(null)
            .setDefaults(0)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .apply {
                if (focusEnabled) setFullScreenIntent(openPending, true)
            }
            .build()

        nm.notify(todoId.toInt(), notif)

        // 6. Phát chuông người dùng chọn
        RingtoneManager.getRingtone(context, ringtoneUri)?.play()
    }
}

class EventReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Nhắc nhở sự kiện"
        val eventId = intent.getLongExtra("eventId", -1L)
        val startDateStr = intent.getStringExtra("startDate") ?: ""
        val startTimeStr = intent.getStringExtra("startTime") ?: ""
        val endDateStr = intent.getStringExtra("endDate") ?: ""
        val endTimeStr = intent.getStringExtra("endTime") ?: ""

        val prefs = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        val ringtoneUriStr = prefs.getString("ringtone_uri", null)
        val ringtoneUri = ringtoneUriStr?.let { Uri.parse(it) }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "event_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Nhắc nhở Sự kiện",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val fullContent = "$startTimeStr – $startDateStr → $endTimeStr – $endDateStr"

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("eventId", eventId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            eventId.toInt(),
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("📅 $title")
            .setContentText(fullContent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(fullContent))
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(null)
            .setDefaults(0)
            .build()

        notificationManager.notify(eventId.toInt(), notification)

        // 🔔 Phát chuông
        RingtoneManager.getRingtone(context, ringtoneUri)?.play()
    }
}