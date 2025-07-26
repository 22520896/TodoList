package com.example.todolist.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todolist.data.entity.Event
import com.example.todolist.data.entity.Todo
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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



object EventReminderScheduler {
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleReminders(context: Context, event: Event) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        event.reminderOffsets.forEachIndexed { index, offset ->
            val triggerAtMillis = event.startTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - offset

            if (triggerAtMillis <= System.currentTimeMillis()) return@forEachIndexed

            val intent = Intent(context, EventReminderReceiver::class.java).apply {
                putExtra("eventId", event.id)
                putExtra("title", event.title)
                putExtra("startDate", event.startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                putExtra("startTime", event.startTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                putExtra("endDate", event.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                putExtra("endTime", event.endTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                "${event.id}_reminder_$index".hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminders(context: Context, event: Event) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        event.reminderOffsets.forEachIndexed { index, _ ->
            val intent = Intent(context, EventReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                "${event.id}_reminder_$index".hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}
