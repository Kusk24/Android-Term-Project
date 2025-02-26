package com.example.androidtermprojectmotopedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtermprojectmotopedia.database.NotificationDatabase
import com.example.androidtermprojectmotopedia.repository.NotificationRepository
import com.example.androidtermprojectmotopedia.model.Notification
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application): AndroidViewModel(application) {

    private val database = NotificationDatabase.getInstance(application)

    val repositoryNotification : NotificationRepository = NotificationRepository(database.notificationDao())

    val liveNotification = repositoryNotification.readAllNotification

    suspend fun addMessages(notification: Notification){
        repositoryNotification.addNotification(notification)
    }


    fun deleteMessages(notification: Notification){
        viewModelScope.launch {
            repositoryNotification.deleteNotification(notification)
        }
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