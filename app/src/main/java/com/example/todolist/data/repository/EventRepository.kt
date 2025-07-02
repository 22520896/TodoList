package com.example.todolist.data.repository


import android.content.Context
import com.example.todolist.alarm.EventReminderScheduler
import com.example.todolist.alarm.ReminderScheduler
import com.example.todolist.data.dao.EventDao
import com.example.todolist.data.entity.Event
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val dao: EventDao,
    @ApplicationContext private val context: Context
) {
    fun getAllEvents(): Flow<List<Event>> = dao.getAllEvents()

    suspend fun insert(event: Event): Long {
        val id = dao.insert(event)
        if (event.reminderOffsets.isNotEmpty()) {
            EventReminderScheduler.scheduleReminders(context, event.copy(id = id))
        }
        return id
    }

    suspend fun update(event: Event) {
        dao.update(event)
        EventReminderScheduler.cancelReminders(context, event)
        if (event.reminderOffsets.isNotEmpty()) {
            EventReminderScheduler.scheduleReminders(context, event)
        }
    }

    suspend fun delete(event: Event) {
        EventReminderScheduler.cancelReminders(context, event)
        dao.delete(event)
    }

    fun getEventById(id: Long): Flow<Event?> = dao.getEventById(id)
}