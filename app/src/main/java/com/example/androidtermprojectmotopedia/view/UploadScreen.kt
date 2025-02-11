package com.example.androidtermprojectmotopedia.view

import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil3.Uri
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
import coil3.toUri

@Composable
fun UploadScreen(modifier : Modifier){

    var selectedImage by remember {
        mutableStateOf<Uri?>("https://icons.veryicon.com/png/o/miscellaneous/ionicons-2/android-upload-2.png".toUri())
    }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()){ uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

// Include only one of the following calls to launch(), depending on the types
// of media that you want to let the user choose from.

// Launch the photo picker and let the user choose images and videos.

    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        val (item1,item2,item3,item4) = createRefs()

        AsyncImage(model = selectedImage, contentDescription = null, modifier = Modifier.clickable {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }.constrainAs(item1){
            top.linkTo(parent.top, 100.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.height(200.dp).border(1.dp, Color.Black, shape = RoundedCornerShape(corner = CornerSize(15.dp),)))

    }
}