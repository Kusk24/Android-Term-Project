package com.example.androidtermprojectmotopedia.view

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
//import coil3.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import coil3.toCoilUri
import coil3.toUri
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.window.Popup
import androidx.core.net.toUri
import java.util.Date
import java.util.Locale

@Composable
fun UploadScreen(modifier : Modifier){

    val scrollState = rememberScrollState()

    var selectedImage by remember {
        mutableStateOf<Uri?>("https://icons.veryicon.com/png/o/miscellaneous/ionicons-2/android-upload-2.png".toUri())
    }

    var mediaType by remember { mutableStateOf<ActivityResultContracts.PickVisualMedia.VisualMediaType?>(null) }

    var selectedVideo by remember {
        mutableStateOf<Uri?>("https://cdn.iconscout.com/icon/free/png-256/free-video-upload-icon-download-in-svg-png-gif-file-formats--uploading-multimedia-vol-2-pack-entertainment-icons-1151629.png".toUri())
    }

    var selectedDate by remember {
        mutableStateOf<Long>(0)
    }

    var showModalInput by remember {
        mutableStateOf(false)
    }

    var brand by remember { mutableStateOf("") }

    var model by remember { mutableStateOf("")}

    var article by remember { mutableStateOf("")}

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()){ uri ->
        if (uri != null) {
            when (mediaType) {
                ActivityResultContracts.PickVisualMedia.ImageOnly -> selectedImage = uri
                ActivityResultContracts.PickVisualMedia.VideoOnly -> selectedVideo = uri
                else -> {} // Do nothing
            }
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        val (item1,item2,item3,item4,item5,item6,item7) = createRefs()

        AsyncImage(model = selectedImage, contentDescription = null, modifier = Modifier.clickable {
            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            pickMedia.launch(PickVisualMediaRequest(mediaType!!))
        }.constrainAs(item1){
            top.linkTo(parent.top, 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.height(200.dp).border(1.dp, Color.Black, shape = RoundedCornerShape(corner = CornerSize(15.dp),)))

        TextField(onValueChange = {
            brand = it
        }, value = brand, modifier = Modifier.constrainAs(item2){
            top.linkTo(item1.bottom, 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, label = {Text("Brand")})

        TextField(onValueChange = {
            model = it
        }, value = model, modifier = Modifier.constrainAs(item7){
            top.linkTo(item2.bottom, 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, label = {Text("Model")})


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.constrainAs(item3) {
            top.linkTo(item7.bottom, 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)}
        ) {
            Text(
                text = convertMillisToDate(selectedDate),
                modifier = Modifier
                    .padding()
                    .wrapContentSize(Alignment.Center)
            )

            Button(
                onClick = { showModalInput = true },
                modifier = Modifier
                    .padding()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text("Choose Date")
            }
        }

        AsyncImage(model = selectedVideo, contentDescription = null, modifier = Modifier.clickable {
            mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
            pickMedia.launch(PickVisualMediaRequest(mediaType!!))
        }.constrainAs(item4){
            top.linkTo(item3.bottom, 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.height(200.dp).border(1.dp, Color.Black, shape = RoundedCornerShape(corner = CornerSize(15.dp),)))


        if (showModalInput) {
            DatePickerModalInput(
                onDateSelected = {
                    if (it != null) {
                        selectedDate = it
                    }
                    showModalInput = false
                },
                onDismiss = { showModalInput = false }
            )
        }

        TextField(value = article, onValueChange = {
            article = it
        },modifier = Modifier.constrainAs(item5){
            top.linkTo(item4.bottom,20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.size(300.dp), label = {
            Text("Article")
        })

        Button(onClick = {}, colors = ButtonDefaults.buttonColors(Color.Green), modifier = Modifier.constrainAs(item6){
            top.linkTo(item5.bottom, 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom, 40.dp)
        }
        ) {
            Text("Upload")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}