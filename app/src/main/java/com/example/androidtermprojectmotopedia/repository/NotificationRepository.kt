package com.example.androidtermprojectmotopedia.repository

import androidx.lifecycle.LiveData
import com.example.androidtermprojectmotopedia.dao.NotificationDao
import com.example.androidtermprojectmotopedia.model.Notification

class NotificationRepository(private val notificationDao: NotificationDao) {

    val readAllNotification : LiveData<List<Notification>> = notificationDao.getAllNotifications()

    suspend fun addNotification(notification: Notification){
        notificationDao.addNotification(notification)
    }

    suspend fun deleteNotification(notification: Notification) {
        notificationDao.deleteNotification(notification)
    }
}