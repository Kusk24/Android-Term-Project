package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.model.Notification
import com.example.androidtermprojectmotopedia.viewModel.NotificationViewModel

@Composable
fun NotificationScreen(modifier: Modifier) {

    val viewModel : NotificationViewModel = viewModel()
    val notificationlists = viewModel.notification.collectAsState().value


        LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp,top = 100.dp)) {
            items(notificationlists) { notification ->
                Noticiation(notification)

                HorizontalDivider(thickness = 1.dp)
            }
        }
}

@Composable
fun Noticiation(notification: Notification){

    ConstraintLayout(modifier = Modifier.fillMaxWidth().height(75.dp).padding(16.dp)) {
        val (item1,item2,item3,item4) = createRefs()

        AsyncImage(model = notification.imageUrl, contentDescription = null,modifier =Modifier.constrainAs(item1){
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        })

        Text(notification.title, fontWeight = FontWeight.SemiBold, modifier = Modifier.constrainAs(item2){
            start.linkTo(item1.end, 10.dp)
            top.linkTo(parent.top)
        })

        Text(notification.body, modifier = Modifier.constrainAs(item3){
            start.linkTo(item1.end, 10.dp)
            top.linkTo(item2.bottom, 10.dp)
            bottom.linkTo(parent.bottom)
        })

    }
}

val demoNotifications = listOf(
    Notification(title = "Notification #1",
        body = "This is the body of notification #1.",
        imageUrl = "Demo reason 1 URL"),

    Notification(title = "Notification #2",
        body = "This is the body of notification #2.",
        imageUrl = "Demo reason 2 URL "),

    Notification(title = "Notification #3",
        body = "This is the body of notification #3.",
        imageUrl = "Demo reason 3 URL"),

    Notification(title = "Notification #4",
        body = "This is the body of notification #4.",
        imageUrl = "Demo reason 4 URL"),

    Notification(title = "Notification #5",
        body = "This is the body of notification #5.",
        imageUrl = "Demo reason 5 URL"),

    Notification(title = "Notification #6",
        body = "This is the body of notification #6.",
        imageUrl = "Demo reason 6 URL"),

    Notification(title = "Notification #7",
        body = "This is the body of notification #7.",
        imageUrl = "Demo reason 7 URL"),

    Notification(title = "Notification #8",
        body = "This is the body of notification #8.",
        imageUrl = "Demo reason 8 URL")
    // … and so on
)