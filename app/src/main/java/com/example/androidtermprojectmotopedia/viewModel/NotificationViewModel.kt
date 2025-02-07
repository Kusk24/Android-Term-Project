package com.example.androidtermprojectmotopedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtermprojectmotopedia.dao.NotificationDatabase
import com.example.androidtermprojectmotopedia.model.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class NotificationViewModel(application: Application): AndroidViewModel(application) {

    private val database = NotificationDatabase.getInstance(application)
    private val _notification = MutableStateFlow<List<Notification>>(emptyList())
    val notification: StateFlow<List<Notification>> get() = _notification

    init{
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch(Dispatchers.IO){
            _notification.value = database.notificationDao().getAllNotifications()
        }
    }

}