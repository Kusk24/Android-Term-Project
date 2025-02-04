package com.example.androidtermprojectmotopedia.View

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androidtermprojectmotopedia.Model.Notification

@Composable
fun NotificationScreen(modifier: Modifier) {

        LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp,top = 40.dp)) {
            items(notificationList) { notification ->
                Noticiation(notification)

                HorizontalDivider(thickness = 1.dp)
            }
        }
}

@Composable
fun Noticiation(notification: Notification){
    Row(modifier = Modifier.height(75.dp)){

        Text(notification.reason)
    }
}

val notificationList = listOf(
    Notification("Hello"),
    Notification("Your order has been shipped!"),
    Notification("New message received"),
    Notification("Your subscription is expiring soon"))