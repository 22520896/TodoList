package com.example.todolist.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.todolist.data.entity.Todo
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

//object ReminderScheduler {
//    @SuppressLint("ScheduleExactAlarm")
//    fun scheduleReminders(context: Context, todo: Todo) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            ?: return
//
//        for ((index, offset) in todo.reminderOffsets.withIndex()) {
//            val triggerAtMillis = todo.startTime
//                .atZone(ZoneId.systemDefault())
//                .toInstant()
//                .toEpochMilli() - offset
//
//            // ✅ Bỏ qua những lịch đã trễ hơn thời điểm hiện tại
//            if (triggerAtMillis <= System.currentTimeMillis()) continue
//
//            val intent = Intent(context, ReminderReceiver::class.java).apply {
//                putExtra("title", todo.title)
//            }
//
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                "${todo.id}_$index".hashCode(),
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//
//            alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                triggerAtMillis,
//                pendingIntent
//            )
//        }
//    }
//
//    fun cancelReminders(context: Context, todo: Todo) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            ?: return
//
//        for ((index, _) in todo.reminderOffsets.withIndex()) {
//            val intent = Intent(context, ReminderReceiver::class.java)
//
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                "${todo.id}_$index".hashCode(),
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//
//            alarmManager.cancel(pendingIntent)
//        }
//    }
//}

//object ReminderScheduler {
//    @SuppressLint("ScheduleExactAlarm")
//    fun scheduleReminders(context: Context, todo: Todo) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//        if (alarmManager == null) {
//            showToast(context, "Lỗi: Không thể lấy AlarmManager")
//            return
//        }
//
//        for ((index, offset) in todo.reminderOffsets.withIndex()) {
//            try {
//                val triggerAtMillis = todo.startTime
//                    .atZone(ZoneId.systemDefault())
//                    .toInstant()
//                    .toEpochMilli() - offset
//
//                if (triggerAtMillis <= System.currentTimeMillis()) continue
//
//                val intent = Intent(context, ReminderReceiver::class.java).apply {
//                    putExtra("title", todo.title)
//                    putExtra("date", todo.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
//                    putExtra("start_time", todo.startTime.format(DateTimeFormatter.ofPattern("HH:mm")))
//                    putExtra("end_time", todo.endTime.format(DateTimeFormatter.ofPattern("HH:mm")))
//                }
//
//                val pendingIntent = PendingIntent.getBroadcast(
//                    context,
//                    "${todo.id}_$index".hashCode(),
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                )
//
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    triggerAtMillis,
//                    pendingIntent
//                )
//            } catch (e: Exception) {
//                Log.e("ReminderScheduler", "Lỗi khi đặt báo thức", e)
//                showToast(context, "Lỗi khi đặt báo thức: ${e.message}")
//            }
//        }
//    }
//
//    private fun showToast(context: Context, message: String) {
//        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
//        }
//    }
//
//    fun cancelReminders(context: Context, todo: Todo) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            ?: return
//
//        for ((index, _) in todo.reminderOffsets.withIndex()) {
//            val intent = Intent(context, ReminderReceiver::class.java)
//
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                "${todo.id}_$index".hashCode(),
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//
//            alarmManager.cancel(pendingIntent)
//        }
//    }
//}
//

object ReminderScheduler {
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleReminders(context: Context, todo: Todo) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // 1. Đặt báo thức nhắc nhở nếu có reminderOffsets
        todo.reminderOffsets.forEachIndexed { index, offset ->
            val triggerAtMillis = todo.startTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - offset

            if (triggerAtMillis <= System.currentTimeMillis()) return@forEachIndexed

            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("todoId", todo.id)
                putExtra("title", todo.title)
                putExtra("isFocusEnabled", todo.isFocusEnabled)
                putExtra("date", todo.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                putExtra("start_time", todo.startTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                putExtra("end_time", todo.endTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                "${todo.id}_reminder_$index".hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }

        // 2. Đặt báo thức mở Focus nếu bật chế độ tập trung
        if (todo.isFocusEnabled) {
            val triggerAtMillis = todo.startTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            if (triggerAtMillis <= System.currentTimeMillis()) return

            val focusIntent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("todoId", todo.id)
                putExtra("title", todo.title)
                putExtra("isFocusEnabled", true) // bắt buộc Focus
                putExtra("date", todo.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                putExtra("start_time", todo.startTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                putExtra("end_time", todo.endTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                "${todo.id}_focus".hashCode(),
                focusIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminders(context: Context, todo: Todo) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // Huỷ các reminder
        todo.reminderOffsets.forEachIndexed { index, _ ->
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                "${todo.id}_reminder_$index".hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }

        // Huỷ focus nếu có
        val focusIntent = Intent(context, ReminderReceiver::class.java)
        val pendingFocus = PendingIntent.getBroadcast(
            context,
            "${todo.id}_focus".hashCode(),
            focusIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingFocus)
    }
}

//object ReminderScheduler {
//
//    private const val TAG = "ReminderScheduler"
//
//    @SuppressLint("ScheduleExactAlarm")
//    fun scheduleReminders(context: Context, todo: Todo) {
//        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            ?: return  // ⛔ service null? dừng ngay.
//
//        for ((idx, offset) in todo.reminderOffsets.withIndex()) {
//
//            val trigger = todo.startTime.atZone(ZoneId.systemDefault())
//                .toInstant().toEpochMilli() - offset
//
//            // 👉 BỎ QUA QUÁ KHỨ
//            if (trigger <= System.currentTimeMillis()) {
//                Log.e(TAG, "Skip past reminder for todo=${todo.id}")
//                continue
//            }
//
//            // **đưa todoId vào Intent**
//            val intent = Intent(context, ReminderReceiver::class.java).apply {
//                putExtra("todoId", todo.id)
//                putExtra("title", todo.title)
//            }
//
//            val pi = PendingIntent.getBroadcast(
//                context,
//                "${todo.id}_$idx".hashCode(),
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//
//            Log.e(TAG, "⏰ schedule ${todo.id} at ${Date(trigger)}")
//            Toast.makeText(context, "Đặt báo thức lúc ${Date(trigger)}", Toast.LENGTH_SHORT).show()
//
//            alarmMgr.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                trigger,
//                pi
//            )
//        }
//    }
//
//        fun cancelReminders(context: Context, todo: Todo) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
//
//        // Huỷ các reminder
//        todo.reminderOffsets.forEachIndexed { index, _ ->
//            val intent = Intent(context, ReminderReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                "${todo.id}_reminder_$index".hashCode(),
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            alarmManager.cancel(pendingIntent)
//        }
//
//        // Huỷ focus nếu có
//        val focusIntent = Intent(context, ReminderReceiver::class.java)
//        val pendingFocus = PendingIntent.getBroadcast(
//            context,
//            "${todo.id}_focus".hashCode(),
//            focusIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        alarmManager.cancel(pendingFocus)
//    }
//}
