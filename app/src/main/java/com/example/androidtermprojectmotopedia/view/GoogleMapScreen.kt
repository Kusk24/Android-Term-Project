package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtermprojectmotopedia.model.Store
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun GoogleMapScreen(brand: String, storeList : List<Store>) {

    val thailand = LatLng(15.8700, 100.9925)

    val cameraPositionState = rememberCameraPositionState(){
        position = CameraPosition.fromLatLngZoom(thailand, 12f)
    }

    Text("${brand}'s Store Location in Thailand",
        fontSize = 20.sp,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp))

    GoogleMap(
        modifier = Modifier.fillMaxWidth().height(700.dp).border(border = BorderStroke(1.dp,
            Color.Black
        )),
        cameraPositionState = cameraPositionState
    ){
        storeList.forEach { store ->
            val storeLatLng = LatLng(store.location.latitude,store.location.longitude)
            val storeName = store.name
            val storeAddress = store.location.address

            Marker(state = rememberMarkerState(position = storeLatLng)
                ,title = storeName, snippet = storeAddress)
        }
    }
}