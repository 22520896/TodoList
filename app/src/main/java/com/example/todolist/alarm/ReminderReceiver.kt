package com.example.todolist.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.todolist.FocusActivity
import com.example.todolist.MainActivity
import com.example.todolist.R

//class ReminderReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        val title = intent.getStringExtra("title") ?: "Nhắc nhở"
//        val dateStr = intent.getStringExtra("date") ?: ""
//        val startTimeStr = intent.getStringExtra("start_time") ?: ""
//        val endTimeStr = intent.getStringExtra("end_time") ?: ""
//
//        // Lấy ringtoneUri từ SharedPreferences
//        val prefs = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
//        val ringtoneUriStr = prefs.getString("ringtone_uri", null)
//        val ringtoneUri = ringtoneUriStr?.let { Uri.parse(it) }
//
//        // Tạo Notification channel
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val channelId = "todo_reminder_channel"
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Nhắc nhở Todo",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Intent để mở ứng dụng khi người dùng bấm vào thông báo
//        val openAppIntent = Intent(context, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            openAppIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val fullContent = "$title\n$dateStr | $startTimeStr → $endTimeStr"
//
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setContentTitle("🔔 Nhắc nhở nhiệm vụ")
//            .setContentText(fullContent)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(fullContent))
//            .setSmallIcon(R.drawable.ic_calendar)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .setSound(null)
//            .setDefaults(0)
//            .build()
//
//        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
//
//        // Phát nhạc chuông
//        val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
//        ringtone?.play()
//    }
//}

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Nhắc nhở"
        val todoId = intent.getLongExtra("todoId", -1L)
        val dateStr = intent.getStringExtra("date") ?: ""
        val startTimeStr = intent.getStringExtra("start_time") ?: ""
        val endTimeStr = intent.getStringExtra("end_time") ?: ""
        val isFocusEnabled = intent.getBooleanExtra("isFocusEnabled", false)

        val prefs = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
        val ringtoneUriStr = prefs.getString("ringtone_uri", null)
        val ringtoneUri = ringtoneUriStr?.let { Uri.parse(it) }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "todo_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Nhắc nhở Todo",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val fullContent = "$title\n$dateStr | $startTimeStr → $endTimeStr"

        val openIntent = Intent(context, if (isFocusEnabled && todoId != -1L) FocusActivity::class.java else MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("todoId", todoId)
        }

        // 👉 Start ngay lập tức nếu focus được bật
        if (isFocusEnabled && todoId != -1L) {
            context.startActivity(openIntent)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            todoId.toInt(),
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("🔔 Nhắc nhở nhiệm vụ")
            .setContentText(fullContent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(fullContent))
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(null)
            .setDefaults(0)
            .build()

        notificationManager.notify(todoId.toInt(), notification)

        // 🔊 Phát chuông
        RingtoneManager.getRingtone(context, ringtoneUri)?.play()
    }
}