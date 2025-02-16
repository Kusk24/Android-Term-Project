package com.example.androidtermprojectmotopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class Store (
    val name : String = "",
    val location : StoreLocation = StoreLocation()
)