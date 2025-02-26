package com.example.androidtermprojectmotopedia.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.androidtermprojectmotopedia.model.Notification
import com.example.androidtermprojectmotopedia.viewModel.NotificationViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: NotificationViewModel = viewModel()
    val notificationList = viewModel.liveNotification.observeAsState().value.orEmpty()

    LazyColumn(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        items(
            items = notificationList,
            key = { it.id }
        ) { notification ->
            Spacer(modifier = Modifier.height(8.dp))
            CustomSwipeToDismissNotificationItem(
                notification = notification,
                onDismissed = {
                    // Remove the notification from your ViewModel/DB
                    viewModel.deleteMessages(notification)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * A custom swipe-to-dismiss implementation that uses pointerInput + offset
 * with detectDragGestures (the newer API), styled for a cleaner look.
 */
@Composable
fun CustomSwipeToDismissNotificationItem(
    notification: Notification,
    onDismissed: () -> Unit,
    threshold: Dp = 120.dp // The minimum horizontal distance to trigger a "dismiss"
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Convert threshold from dp to px
    val thresholdPx = with(LocalDensity.current) { threshold.toPx() }

    var itemWidth by remember { mutableStateOf(0f) }

    // Outer box that holds both background + foreground
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { cords ->
                itemWidth = cords.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consumePositionChange()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            val absOffset = offsetX.value.absoluteValue
                            if (absOffset > thresholdPx) {
                                val target = if (offsetX.value > 0) itemWidth else -itemWidth
                                offsetX.animateTo(
                                    targetValue = target,
                                    animationSpec = tween(durationMillis = 300)
                                )
                                onDismissed()
                            } else {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 300)
                                )
                            }
                        }
                    }
                )
            }
    ) {
        // 1) Full-size background
        Box(
            modifier = Modifier
                .matchParentSize() // Fill exactly the same size as the card
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Red),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
        }

        // 2) Foreground content (the actual notification UI) - offset horizontally
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.toInt(), 0) }
        ) {
            NotificationItemUI(notification = notification)
        }
    }
}

/**
 * A Card + ConstraintLayout for the notification details, with some styling.
 */
@Composable
fun NotificationItemUI(notification: Notification) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight() // Let content define height
        ) {
            val (imageRef, titleRef, bodyRef) = createRefs()

            // Thumbnail
            AsyncImage(
                model = notification.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small)
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            // Title
            Text(
                text = notification.title,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(titleRef) {
                    start.linkTo(imageRef.end, margin = 12.dp)
                    top.linkTo(parent.top)
                }
            )

            // Body
            Text(
                text = notification.body,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.constrainAs(bodyRef) {
                    start.linkTo(imageRef.end, margin = 12.dp)
                    top.linkTo(titleRef.bottom, margin = 4.dp)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}
