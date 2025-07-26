package com.example.todolist.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.todolist.data.dao.SettingDao
import com.example.todolist.data.entity.Setting
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val settingDao: SettingDao,
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)

    companion object {
        private const val RINGTONE_URI_KEY = "ringtone_uri"
        private const val RINGTONE_NAME_KEY = "ringtone_name"
    }

    // Room-based Settings
    suspend fun insertSetting(setting: Setting) = settingDao.insertSetting(setting)
    suspend fun updateDateFormat(format: String) = settingDao.updateDateFormat(format)
    suspend fun updateTimeFormat(format: String) = settingDao.updateTimeFormat(format)
    suspend fun updateColor(color: String) = settingDao.updateColor(color)
    fun getSetting(): Flow<Setting> = settingDao.getSetting()

    // SharedPreferences-based Ringtone
    suspend fun saveRingtone(uri: String, name: String) {
        settingDao.updateRingtone(uri, name)
        prefs.edit().putString(RINGTONE_URI_KEY, uri).putString(RINGTONE_NAME_KEY, name).apply()
    }

    fun getRingtoneUri(): String = prefs.getString(RINGTONE_URI_KEY, "") ?: ""
    fun getRingtoneName(): String = prefs.getString(RINGTONE_NAME_KEY, "") ?: ""
}