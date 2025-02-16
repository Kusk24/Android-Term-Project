package com.example.androidtermprojectmotopedia.model

import kotlinx.serialization.Serializable

@Serializable
data class BrandApiWrapper (
    val brands : List<Brand> = listOf()
)