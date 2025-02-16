package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidtermprojectmotopedia.model.Store
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun GoogleMapScreen(storeList : List<Store>) {

    val thailand = LatLng(15.8700, 100.9925)

    val cameraPositionState = rememberCameraPositionState(){
        position = CameraPosition.fromLatLngZoom(thailand, 12f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxWidth().height(700.dp),
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