package com.example.androidtermprojectmotopedia.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtermprojectmotopedia.database.NotificationDatabase
import com.example.androidtermprojectmotopedia.repository.NotificationRepository
import com.example.androidtermprojectmotopedia.model.Notification
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application): AndroidViewModel(application) {

    private val database = NotificationDatabase.getInstance(application)
    val repositoryNotification: NotificationRepository = NotificationRepository(database.notificationDao())
    val liveNotification = repositoryNotification.readAllNotification

    suspend fun addMessages(notification: Notification) {
        repositoryNotification.addNotification(notification)
    }

    fun deleteMessages(notification: Notification) {
        viewModelScope.launch {
            repositoryNotification.deleteNotification(notification)
        }
    }

    fun subscribeToTopic() {
        // Use getApplication() to access the context from AndroidViewModel
        getApplication<Application>().let { appContext ->
            Firebase.messaging.subscribeToTopic("NewArticle")
                .addOnCompleteListener { task ->
                    val msg = if (task.isSuccessful) "Subscribed" else "Subscription failed"
                    Log.d("NotificationVM", msg)
                    Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun unsubscribeFromTopic() {
        getApplication<Application>().let { appContext ->
            Firebase.messaging.unsubscribeFromTopic("NewArticle")
                .addOnCompleteListener { task ->
                    val msg = if (task.isSuccessful) "Unsubscribed" else "Unsubscribe failed"
                    Log.d("NotificationVM", msg)
                    Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun logRegistrationToken() {
        getApplication<Application>().let { appContext ->
            Firebase.messaging.token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("NotificationVM", "Fetching token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("NotificationVM", "Token: $token")
                Toast.makeText(appContext, "Token: $token", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
