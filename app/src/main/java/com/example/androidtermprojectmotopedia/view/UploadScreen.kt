package com.example.androidtermprojectmotopedia.view

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import java.util.Date
import java.util.Locale

@Composable
fun UploadScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    // States for image/video picking
    var selectedImage by remember {
        mutableStateOf<Uri?>(
            "https://icons.veryicon.com/png/o/miscellaneous/ionicons-2/android-upload-2.png".toUri()
        )
    }
    var selectedVideo by remember {
        mutableStateOf<Uri?>(
            "https://cdn.iconscout.com/icon/free/png-256/free-video-upload-icon-download-in-svg-png-gif-file-formats--uploading-multimedia-vol-2-pack-entertainment-icons-1151629.png".toUri()
        )
    }
    var mediaType by remember {
        mutableStateOf<ActivityResultContracts.PickVisualMedia.VisualMediaType?>(null)
    }

    // States for brand, model, article
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var article by remember { mutableStateOf("") }

    // States for date picking
    var selectedDate by remember { mutableStateOf<Long>(0L) }
    var showModalInput by remember { mutableStateOf(false) }

    // Setup pickMedia launcher
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            when (mediaType) {
                ActivityResultContracts.PickVisualMedia.ImageOnly -> {
                    selectedImage = uri
                }
                ActivityResultContracts.PickVisualMedia.VideoOnly -> {
                    selectedVideo = uri
                }
                else -> Unit
            }
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    // Main Surface background
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // A simple title
            Text(
                text = "Upload a Article",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Row with image and video side-by-side
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Image box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            pickMedia.launch(PickVisualMediaRequest(mediaType!!))
                        }.background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = selectedImage,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                // Video box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
                            pickMedia.launch(PickVisualMediaRequest(mediaType!!))
                        }.background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = selectedVideo,
                        contentDescription = "Selected Video",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            // Brand field
            TextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Brand") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            // Model field
            TextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Model") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            // Row for date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = convertMillisToDate(selectedDate))
                Button(onClick = { showModalInput = true }) {
                    Text("Choose Date")
                }
            }

            // Article input in a Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                TextField(
                    value = article,
                    onValueChange = { article = it },
                    label = { Text("Article") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(8.dp)
                )
            }

            // Upload button
            Button(
                onClick = {
                    // TODO: implement your upload logic
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Upload", color = Color.White)
            }
        }

        // DatePicker dialog if needed
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

/**
 * Helper function to convert a millis timestamp into a string date.
 * If 0L, defaults to "01/01/1970".
 */
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return if (millis == 0L) "01/01/1970" else formatter.format(Date(millis))
}
