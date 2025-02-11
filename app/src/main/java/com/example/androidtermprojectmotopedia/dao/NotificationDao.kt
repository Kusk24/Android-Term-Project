package com.example.androidtermprojectmotopedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidtermprojectmotopedia.model.Notification
import com.google.firebase.firestore.auth.User

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNotification(notification: Notification)

//    @Query("SELECT * FROM notification_history ORDER BY timestamp DESC")
//    fun getAllNotifications(): List<Notification>

    @Query("SELECT * FROM notification_history ORDER BY timestamp DESC")
    fun getAllNotifications(): LiveData<List<Notification>>

}