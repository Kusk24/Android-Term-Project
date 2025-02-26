package com.example.androidtermprojectmotopedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidtermprojectmotopedia.database.NotificationDatabase
import com.example.androidtermprojectmotopedia.repository.NotificationRepository
import com.example.androidtermprojectmotopedia.model.Notification

class NotificationViewModel(application: Application): AndroidViewModel(application) {

    private val database = NotificationDatabase.getInstance(application)

    val repositoryNotification : NotificationRepository = NotificationRepository(database.notificationDao())

    val liveNotification = repositoryNotification.readAllNotification

    suspend fun addMessages(notification: Notification){
        repositoryNotification.addNotification(notification)
    }


//    private val database = NotificationDatabase.getInstance(application)
//    private val _notification = MutableStateFlow<List<Notification>>(emptyList())
//    val notification: StateFlow<List<Notification>> get() = _notification
//
//    init{
//        loadMessages()
//    }
//
//    private fun loadMessages() {
//        viewModelScope.launch(Dispatchers.IO){
//            _notification.value = database.notificationDao().getAllNotifications()
//        }
//    }
}