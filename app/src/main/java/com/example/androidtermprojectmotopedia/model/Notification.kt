package com.example.androidtermprojectmotopedia.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_history")
data class Notification (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title : String,
    val body : String,
    val imageUrl: String?,
    val timestamp: Long = System.currentTimeMillis()
)