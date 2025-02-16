package com.example.androidtermprojectmotopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreLocation (
    val latitude : Double = 15.8700,
    val longitude : Double = 100.9925,
    val address : String = ""
)