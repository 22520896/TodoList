package com.example.todolist.utils

import android.content.Context
import android.media.RingtoneManager


fun defaultRingtone(context: Context): Pair<String, String> {
    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

    val r = RingtoneManager.getRingtone(context, uri)
    val name = r?.getTitle(context) ?: "Default"

    return uri.toString() to name
}

fun getRingtones(context: Context): List<Pair<String, String>> {
    val ringtoneManager = RingtoneManager(context).apply {
        setType(RingtoneManager.TYPE_NOTIFICATION)
    }
    val cursor = ringtoneManager.cursor
    val ringtones = mutableListOf<Pair<String, String>>()

    while (cursor.moveToNext()) {
        val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
        val uri = ringtoneManager.getRingtoneUri(cursor.position)?.toString()
        if (uri != null) {
            ringtones.add(title to uri)
        }
    }
    return ringtones
}