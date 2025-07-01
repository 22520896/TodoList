package com.example.todolist.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todolist.data.entity.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: Setting)

    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSetting(): Flow<Setting>

    @Query("UPDATE settings SET dateFormat = :format WHERE id = 1")
    suspend fun updateDateFormat(format: String)

    @Query("UPDATE settings SET timeFormat = :format WHERE id = 1")
    suspend fun updateTimeFormat(format: String)

    @Query("UPDATE settings SET color = :color WHERE id = 1")
    suspend fun updateColor(color: String)

    @Query("UPDATE settings SET ringtoneUri = :uri, ringtoneName = :name WHERE id = 1")
    suspend fun updateRingtone(uri: String, name: String)
}